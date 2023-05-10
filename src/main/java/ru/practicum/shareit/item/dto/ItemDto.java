package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;
}
