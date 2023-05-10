package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStartDate(bookingDto.getStart());
        booking.setEndDate(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingResponseDto toBookingReturnDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStartDate());
        bookingResponseDto.setEnd(booking.getEndDate());
        bookingResponseDto.setItem(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()));
        bookingResponseDto.setBooker(new UserShortDto(booking.getBooker().getId()));
        bookingResponseDto.setStatus(booking.getStatus());
        return bookingResponseDto;
    }

    public static BookingResponseShortDto toBookingShortDto(Booking booking) {
        BookingResponseShortDto bookingResponseShortDto = new BookingResponseShortDto();
        if (booking != null) {
            bookingResponseShortDto.setId(booking.getId());
            bookingResponseShortDto.setBookerId(booking.getBooker().getId());
        } else {
            bookingResponseShortDto = null;
        }
        return bookingResponseShortDto;
    }

    public static List<BookingResponseDto> bookingListToBookingReturnDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingReturnDto).collect(Collectors.toList());
    }
}
