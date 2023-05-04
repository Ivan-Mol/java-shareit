package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

import java.util.List;

public interface BookingService {
    BookingReturnDto getById(Long bookingId, Long userId);

    BookingReturnDto create(BookingDto bookingDto, Long userId);

    List<BookingReturnDto> getAllByOwner(Long bookerId, String state);
}
