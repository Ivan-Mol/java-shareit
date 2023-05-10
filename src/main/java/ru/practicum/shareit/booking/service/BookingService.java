package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto getById(Long bookingId, Long userId);

    BookingResponseDto create(BookingDto bookingDto, Long userId);

    List<BookingResponseDto> getAllByBooker(Long bookerId, String state);

    BookingResponseDto approvingByOwner(Long bookingId, Long ownerId, Boolean approved);

    List<BookingResponseDto> getAllByOwner(Long ownerId, String state);
}
