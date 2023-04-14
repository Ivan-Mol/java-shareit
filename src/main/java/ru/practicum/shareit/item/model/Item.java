package ru.practicum.shareit.item.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.ItemRequest;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
}
