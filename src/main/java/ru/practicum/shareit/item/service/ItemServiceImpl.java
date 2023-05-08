package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
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

    @Override
    public ItemDto getById(Long id, Long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with " + id + " Id is not found"));
        if (item.getOwner().getId().equals(ownerId)) {
            ItemDto itemDto = ItemMapper.toItemDto(item);
            Booking lastBooking = bookingRepository
                    .getFirstByItemIdAndEndDateBeforeAndStatusOrderByStartDateAsc(itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
            Booking nextBooking = bookingRepository
                    .getFirstByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(itemDto.getId(), LocalDateTime.now(),BookingStatus.APPROVED);
            itemDto.setLastBooking(BookingMapper.toBookingShortDto(lastBooking));
            itemDto.setNextBooking(BookingMapper.toBookingShortDto(nextBooking));
            return itemDto;
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        return itemRepository.getByOwnerId(user.getId()).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());

//        List<Item> items = itemRepository.getByOwnerId(user.getId());
//        List<Booking> lastBookings = bookingRepository.getAllByEndDateBeforeAndItemIn(LocalDateTime.now(),items);
//        List<Booking> nextBookings = bookingRepository.getAllByEndDateAfterAndItemIn(LocalDateTime.now(),items);
//        ArrayList<ItemDto> result = null;
//        for (Item item:items) {
//            ItemDto itemDto = ItemMapper.toItemDto(item);
//            for (Booking lastBooking : lastBookings) {
//                if (lastBooking.getItem().getId() == itemDto.getId()) {
//                    itemDto.setLastBooking(BookingMapper.toBookingShortDto(lastBooking));
//                }
//            }
//            for (Booking nextBooking : nextBookings) {
//                if (nextBooking.getItem().getId() == itemDto.getId()) {
//                    itemDto.setNextBooking(BookingMapper.toBookingShortDto(nextBooking));
//                }
//            }
//            result.add(itemDto);
//        }
//        return result;
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getItemsByQuery(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
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

}
