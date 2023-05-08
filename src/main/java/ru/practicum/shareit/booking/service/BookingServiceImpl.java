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
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingReturnDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with " + bookingId + " Id is not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with this id " + userId + " not found"));
        if (booking.getItem().getOwner().getId().equals(user.getId()) || booking.getBooker().getId().equals(user.getId())) {
            return BookingMapper
                    .toBookingReturnDto(booking);
        } else {
            throw new NotFoundException("User is not owner or booker");
        }
    }

    @Override
    public BookingReturnDto create(BookingDto bookingDto, Long userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with this id " + userId + " not found"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with " + bookingDto.getItemId() + " Id is not found"));
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Booker is equals owner");
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
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        return BookingMapper.toBookingReturnDto(bookingRepository.save(booking));
    }

    @Override
    public BookingReturnDto approvingByOwner(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with " + bookingId + " Id is not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        if (approved == null) {
            throw new ValidationException("status is null");
        }
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new ValidationException("Start date is after end date");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new ValidationException("Start date is equal end date");
        }
        if (booking.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Can not start in the past");
        }
        if (!owner.getId().equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("User is not this booking owner");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Booking is already approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingReturnDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingReturnDto> getAllByOwner(Long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + ownerId + " not found"));
        ArrayList<Booking> result = null;
        switch (state) {
            case "ALL":
                result = bookingRepository.getAllByItem_OwnerIdOrderByStartDateDesc(ownerId);
                break;
            case "PAST":
                result = bookingRepository.getAllByItem_OwnerIdAndEndDateIsBeforeOrderByStartDateDesc(ownerId, LocalDateTime.now());
                break;
            case "CURRENT":
                result = bookingRepository.getAllByItem_Owner_IdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(ownerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "FUTURE":
                result = bookingRepository.getAllByItem_OwnerIdAndStartDateIsAfterOrderByStartDateDesc(ownerId, LocalDateTime.now());
                break;
            case "WAITING":
                result = bookingRepository.getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(ownerId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                result = bookingRepository.getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(ownerId, BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return result
                .stream()
                .map(BookingMapper::toBookingReturnDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingReturnDto> getAllByBooker(Long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User with this id " + bookerId + " not found"));
        ArrayList<Booking> result = null;
        switch (state) {
            case "ALL":
                result = bookingRepository.getAllByBookerIdOrderByStartDateDesc(bookerId);
                break;
            case "PAST":
                result = bookingRepository.getAllByBookerIdAndEndDateIsBeforeOrderByStartDateDesc(bookerId, LocalDateTime.now());
                break;
            case "CURRENT":
                result = bookingRepository.getAllByBookerIdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case "FUTURE":
                result = bookingRepository.getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(bookerId, LocalDateTime.now());
                break;
            case "WAITING":
                result = bookingRepository.getAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                result = bookingRepository.getAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");

        }
        return result
                .stream()
                .map(BookingMapper::toBookingReturnDto).collect(Collectors.toList());
    }
}
