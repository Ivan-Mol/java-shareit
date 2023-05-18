package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto getById(Long id, Long userId) {
        Item item = itemRepository.getByIdAndCheck(id);
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.getByIdAndCheck(id));
        itemDto.setComments(commentRepository.getAllByItemId(id));
        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime currentTime = LocalDateTime.now();
            itemDto.setLastBooking(getLastBooking(id, currentTime));
            itemDto.setNextBooking(getNextBooking(id, currentTime));
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User owner = userRepository.getByIdAndCheck(ownerId);
        List<Item> items = itemRepository.getByOwnerId(owner.getId());
        return getItemsDtoWithLastAndNextBookings(items);
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        return text.isBlank() ?
                new ArrayList<>() :
                ItemMapper.itemlistToitemDtolist(itemRepository.getItemsByQuery(text));
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User user = userRepository.getByIdAndCheck(ownerId);
        Item item = ItemMapper.toItem(itemDto, user);
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId()).orElse(new Request());
        }
        item.setRequest(request);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item oldItem = itemRepository.getByIdAndCheck(itemId);
        User owner = userRepository.getByIdAndCheck(ownerId);
        Item item = ItemMapper.toItem(itemDto, owner);
        if (item.getOwner() == null) {
            item.setOwner(owner);
        }
        if (!oldItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("user is incorrect");
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

    public List<ItemDto> getItemsDtoWithLastAndNextBookings(List<Item> items) {
        List<Booking> lastBookings = bookingRepository
                .getAllByEndDateBeforeAndStatusAndItemInOrderByStartDateDesc(LocalDateTime.now(), BookingStatus.APPROVED, items);
        List<Booking> nextBookings = bookingRepository
                .getAllByStartDateAfterAndStatusAndItemInOrderByStartDateDesc(LocalDateTime.now(), BookingStatus.APPROVED, items);
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            for (Booking lastBooking : lastBookings) {
                if (lastBooking.getItem().getId() == itemDto.getId()) {
                    itemDto.setLastBooking(lastBooking);
                }
            }
            for (Booking nextBooking : nextBookings) {
                if (nextBooking.getItem().getId() == itemDto.getId()) {
                    itemDto.setNextBooking(nextBooking);
                }
            }
            result.add(itemDto);
        }
        return result;
    }

    @Override
    public CommentDto createComment(Long itemId, CommentDto commentDto, Long userId) {
        Item item = itemRepository.getByIdAndCheck(itemId);
        User user = userRepository.getByIdAndCheck(userId);
        LocalDateTime timeOfCreation = LocalDateTime.now();
        commentDto.setCreated(timeOfCreation);
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndItem_IdAndStatusAndEndDateIsBefore(
                userId,
                itemId,
                BookingStatus.APPROVED,
                timeOfCreation);
        if (!bookings.isEmpty()) {
            Comment comment = commentRepository.save(CommentMapper.toComment(commentDto, user, item));
            return CommentMapper.toCommentDto(comment);
        } else {
            throw new ValidationException("user with id " + userId + " has no bookings with item id " + item.getId());
        }
    }

    public Booking getLastBooking(Long itemId, LocalDateTime currentTime) {
        return bookingRepository
                .getFirstByItemIdAndStatusAndStartDateIsBeforeOrderByStartDateDesc(itemId, BookingStatus.APPROVED, currentTime);
    }

    public Booking getNextBooking(Long itemId, LocalDateTime currentTime) {
        return bookingRepository
                .getFirstByItemIdAndStatusAndStartDateIsAfterOrderByStartDateAsc(itemId, BookingStatus.APPROVED, currentTime);
    }
}
