package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStartDate());
        bookingDto.setEnd(booking.getEndDate());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;

    }

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

    public static BookingReturnDto toBookingReturnDto(Booking booking) {
        BookingReturnDto bookingReturnDto = new BookingReturnDto();
        bookingReturnDto.setId(booking.getId());
        bookingReturnDto.setStart(booking.getStartDate());
        bookingReturnDto.setEnd(booking.getEndDate());
        bookingReturnDto.setItem(new ItemShortDto(booking.getItem().getId(), booking.getItem().getName()));
        bookingReturnDto.setBooker(new UserShortDto(booking.getBooker().getId()));
        bookingReturnDto.setStatus(booking.getStatus());
        return bookingReturnDto;
    }
}
