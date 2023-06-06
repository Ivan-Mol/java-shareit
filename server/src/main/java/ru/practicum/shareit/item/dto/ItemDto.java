package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingResponseShortDto lastBooking;
    private BookingResponseShortDto nextBooking;
    private List<CommentDto> comments;

    public void setLastBooking(Booking booking) {
        this.lastBooking = BookingMapper.toBookingShortDto(booking);
    }

    public void setNextBooking(Booking booking) {
        this.nextBooking = BookingMapper.toBookingShortDto(booking);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
