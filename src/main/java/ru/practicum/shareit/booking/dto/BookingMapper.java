package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

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

    public static BookingShortDto toBookingShortDto(Booking booking) {
        BookingShortDto bookingShortDto = new BookingShortDto();
        if (booking != null) {
            bookingShortDto.setId(booking.getId());
            bookingShortDto.setBookerId(booking.getBooker().getId());
        } else {
            bookingShortDto = null;
        }
        return bookingShortDto;
    }
}
