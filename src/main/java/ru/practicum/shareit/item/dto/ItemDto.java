package ru.practicum.shareit.item.dto;

import jdk.jfr.BooleanFlag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Long ownerId;
    private ItemRequest request;
}
