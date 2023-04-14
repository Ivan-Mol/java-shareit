package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
}
