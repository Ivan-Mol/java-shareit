package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingReturnDto getById(Long bookingid, Long userId) {
        Booking booking = bookingRepository
                .findById(bookingid)
                .orElseThrow(() -> new NotFoundException("Booking with " + bookingid + " Id is not found"));
        UserDto userDto = userService.getById(userId);
        if (!booking.getBooker().getId().equals(userDto.getId())) {
            throw new ValidationException("User is not item owner");
        }
        return BookingMapper
                .toBookingReturnDto(bookingRepository
                        .findById(userId)
                        .orElseThrow(() -> new NotFoundException("Booking with " + bookingid + " Id is not found")));
    }

    @Override
    public BookingReturnDto create(BookingDto bookingDto, Long userId) {
        bookingDto.setStatus(BookingStatus.WAITING);
        User booker = UserMapper.toUser(userService.getById(userId));
        User owner = itemService.getOwner(bookingDto.getItemId());
        Item item = ItemMapper.toItem(itemService.getById(bookingDto.getItemId()), owner);
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new ValidationException("Booker is equals owner");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Start date is after end date");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Start date is equal end date");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Can not start in the past");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Can not end in the past");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        return BookingMapper.toBookingReturnDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingReturnDto> getAllByOwner(Long bookerId, String state) {
        userService.getById(bookerId);
        if (state == null) {
            state = "ALL";
        }
        ArrayList<Booking> result = null;
        switch (state) {
            case "ALL":
                result = bookingRepository.getAllByBookerIdOrderByStartDateDesc(bookerId);
                break;
            case "PAST":
                result = bookingRepository.getAllByBookerIdAndStartDateIsBeforeOrderByStartDateDesc(bookerId, LocalDateTime.now());
                break;
            case "FUTURE":
                result = bookingRepository.getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(bookerId, LocalDateTime.now());
                break;

        }
        return result
                .stream()
                .map(BookingMapper::toBookingReturnDto).collect(Collectors.toList());
    }
}
