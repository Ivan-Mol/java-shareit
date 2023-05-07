package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

import java.util.List;

public interface BookingService {
    BookingReturnDto getById(Long bookingId, Long userId);

    BookingReturnDto create(BookingDto bookingDto, Long userId);

    List<BookingReturnDto> getAllByBooker(Long bookerId, String state);

    BookingReturnDto approvingByOwner(Long bookingId, Long ownerId, Boolean approved);

    List<BookingReturnDto> getAllByOwner(Long ownerId, String state);
}
