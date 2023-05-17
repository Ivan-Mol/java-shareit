package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemShortForRequestDto {
    private Long id;
    private String name;
    private String Description;
    private Boolean available;
    private Long requestId;
}
