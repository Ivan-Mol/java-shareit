package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "description is null or empty")
    private String name;
    @NotBlank(message = "description is null or empty")
    private String description;
    @NotNull(message = "available is null")
    private Boolean available;
    private Long requestId;

}
