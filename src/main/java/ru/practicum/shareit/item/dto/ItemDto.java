package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
    private BookingReturnDto lastBooking;
    private BookingReturnDto nextBooking;
}
