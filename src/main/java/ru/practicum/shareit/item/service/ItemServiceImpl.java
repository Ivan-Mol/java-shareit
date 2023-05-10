package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto getById(Long id, Long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with " + id + " Id is not found"));
        List<Comment> commentsOfItem = commentRepository.getAllByItemId(id);
        if (item.getOwner().getId().equals(ownerId)) {
            ItemDto itemDto = getItemDtoWithBookings(item);
            itemDto.setComments(commentsOfItem.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
            return itemDto;
        }
        ItemDto itemDto= ItemMapper.toItemDto(item);
        itemDto.setComments(commentsOfItem.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        List<Item> items = itemRepository.getByOwnerId(user.getId());
        return getItemsDtoWithBookings(items);
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getItemsByQuery(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long itemId, CommentDto commentDto, long userId) {
        if (commentDto.getText().equals("")) {
            throw new ValidationException("text is empty");
        }
        commentDto.setCreated(LocalDateTime.now());
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with " + itemId + " Id is not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with this id " + userId + " not found"));
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndItem_IdAndStatusAndEndDateIsBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());
        if (!bookings.isEmpty()) {
            Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, user, item));
            return CommentMapper.toCommentDto(comment);
        } else {
            throw new ValidationException("User with id " + userId + " has no bookings with item id " + item.getId());
        }
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Item is Not Available");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("Description Is Null");
        }
        if (itemDto.getName() == null || itemDto.getName().equals("")) {
            throw new ValidationException("Name is Invalid");
        }
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, user);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with " + itemId + " Id is not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        if (item.getOwner() == null) {
            item.setOwner(owner);
        }
        if (!oldItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("User is incorrect");
        }
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        item.setId(itemId);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public List<ItemDto> getItemsDtoWithBookings(List<Item> items) {
        List<Booking> lastBookings = bookingRepository
                .getAllByEndDateBeforeAndStatusAndItemInOrderByStartDateDesc(LocalDateTime.now(), BookingStatus.APPROVED, items);
        List<Booking> nextBookings = bookingRepository
                .getAllByStartDateAfterAndStatusAndItemInOrderByStartDateDesc(LocalDateTime.now(), BookingStatus.APPROVED, items);
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            for (Booking lastBooking : lastBookings) {
                if (lastBooking.getItem().getId() == itemDto.getId()) {
                    itemDto.setLastBooking(BookingMapper.toBookingShortDto(lastBooking));
                }
            }
            for (Booking nextBooking : nextBookings) {
                if (nextBooking.getItem().getId() == itemDto.getId()) {
                    itemDto.setNextBooking(BookingMapper.toBookingShortDto(nextBooking));
                }
            }
            result.add(itemDto);
        }
        return result;
    }

    public ItemDto getItemDtoWithBookings(Item item) {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        LocalDateTime localDateTime = LocalDateTime.now();
        Booking lastBooking = bookingRepository
                .getFirstByItemIdAndEndDateBeforeAndStatusOrderByStartDateDesc(itemDto.getId(), localDateTime,BookingStatus.APPROVED);
        Booking nextBooking = bookingRepository
                .getFirstByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(itemDto.getId(), localDateTime,BookingStatus.APPROVED);
        itemDto.setLastBooking(BookingMapper.toBookingShortDto(lastBooking));
        itemDto.setNextBooking(BookingMapper.toBookingShortDto(nextBooking));
        return itemDto;
    }
}
