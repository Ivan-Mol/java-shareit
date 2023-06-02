package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private long id;
    @NotBlank(message = "description is null or empty")
    private String name;
    @NotBlank(message = "description is null or empty")
    private String description;
    @NotNull(message = "available is null")
    private Boolean available;
    private Long requestId;
//    private BookingResponseShortDto lastBooking;
//    private BookingResponseShortDto nextBooking;
    //private List<CommentDto> comments;

//    public void setLastBooking(Booking booking) {
//        this.lastBooking = BookingMapper.toBookingShortDto(booking);
//    }
//
//    public void setNextBooking(Booking booking) {
//        this.nextBooking = BookingMapper.toBookingShortDto(booking);
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
//    }
}
