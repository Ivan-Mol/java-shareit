package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.getByIdAndCheck(bookingId);
        User user = userRepository.getByIdAndCheck(userId);
        if (booking.getItem().getOwner().getId().equals(user.getId()) || booking.getBooker().getId().equals(user.getId())) {
            return BookingMapper.toBookingReturnDto(booking);
        } else {
            throw new NotFoundException("User is not owner or booker");
        }
    }

    @Override
    public BookingResponseDto create(BookingDto bookingDto, Long userId) {
        User booker = userRepository.getByIdAndCheck(userId);
        Item item = itemRepository.getByIdAndCheck(bookingDto.getItemId());
        if (booker.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Booker is equals owner");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        bookingDateCheck(booking);
        return BookingMapper.toBookingReturnDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public BookingResponseDto approvingByOwner(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.getByIdAndCheck(bookingId);
        User owner = userRepository.getByIdAndCheck(ownerId);
        if (approved == null) {
            throw new ValidationException("Status is null");
        }
        bookingDateCheck(booking);
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
    public List<BookingResponseDto> getAllByOwner(Long ownerId, String state, Integer from, Integer size) {
        userRepository.getByIdAndCheck(ownerId);
        LocalDateTime currentTime = LocalDateTime.now();
        ArrayList<Booking> result = null;
        PageRequest request = PageRequest.of(from, size);
        switch (state) {
            case "ALL":
                result = bookingRepository.getAllByItem_OwnerIdOrderByStartDateDesc(ownerId, request);
                break;
            case "PAST":
                result = bookingRepository.getAllByItem_OwnerIdAndEndDateIsBeforeOrderByStartDateDesc(ownerId, currentTime, request);
                break;
            case "CURRENT":
                result = bookingRepository.getAllByItem_Owner_IdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(ownerId, currentTime, currentTime, request);
                break;
            case "FUTURE":
                result = bookingRepository.getAllByItem_OwnerIdAndStartDateIsAfterOrderByStartDateDesc(ownerId, currentTime, request);
                break;
            case "WAITING":
                result = bookingRepository.getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(ownerId, BookingStatus.WAITING, request);
                break;
            case "REJECTED":
                result = bookingRepository.getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(ownerId, BookingStatus.REJECTED, request);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.bookingListToBookingReturnDtoList(result);
    }

    @Override
    public List<BookingResponseDto> getAllByBooker(Long bookerId, String state, Integer from, Integer size) {
        userRepository.getByIdAndCheck(bookerId);
        LocalDateTime currentTime = LocalDateTime.now();
        ArrayList<Booking> result = null;
        PageRequest request = PageRequest.of(from/size, size);
        switch (state) {
            case "ALL":
                result = bookingRepository.getAllByBookerIdOrderByStartDateDesc(bookerId, request);
                break;
            case "PAST":
                result = bookingRepository.getAllByBookerIdAndEndDateIsBeforeOrderByStartDateDesc(bookerId, currentTime, request);
                break;
            case "CURRENT":
                result = bookingRepository.getAllByBookerIdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(bookerId, currentTime, currentTime, request);
                break;
            case "FUTURE":
                result = bookingRepository.getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(bookerId, currentTime, request);
                break;
            case "WAITING":
                result = bookingRepository.getAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.WAITING, request);
                break;
            case "REJECTED":
                result = bookingRepository.getAllByBookerIdAndStatusOrderByStartDateDesc(bookerId, BookingStatus.REJECTED, request);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return BookingMapper.bookingListToBookingReturnDtoList(result);
    }

    private void bookingDateCheck(Booking booking) {
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new ValidationException("Start date is after end date");
        }
        if (booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new ValidationException("Start date is equal end date");
        }
        if (booking.getStartDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Can not start in the past");
        }
        if (booking.getEndDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Can not end in the past");
        }
    }
}
