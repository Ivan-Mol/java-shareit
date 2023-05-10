package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ItemDto {
    private long id;
    @NotBlank(message = "description is null or empty")
    private String name;
    @NotBlank(message = "description is null or empty")
    private String description;
    @NotNull(message = "available is null")
    private Boolean available;
    private ItemRequest request;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;

    public void setLastBooking(Booking booking){
        this.lastBooking = BookingMapper.toBookingShortDto(booking);
    }
    public void setNextBooking(Booking booking){
        this.nextBooking = BookingMapper.toBookingShortDto(booking);
    }
    public void setComments(List<Comment> comments){
        this.comments = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
