package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ItemDto {
    @NotNull(groups = Update.class)
    private Long id;
    @NotBlank(message = "name is null or empty", groups = Create.class)
    private String name;
    @NotBlank(message = "description is null or empty", groups = Create.class)
    private String description;
    @NotNull(message = "available is null", groups = Create.class)
    private Boolean available;
    private Long requestId;

}
