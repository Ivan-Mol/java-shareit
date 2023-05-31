package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class ItemForRequestDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
