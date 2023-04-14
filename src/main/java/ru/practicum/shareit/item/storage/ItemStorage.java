package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, Long ownerId);

    Item update(Long itemId, Item item, long ownerId);

    Item getById(long id);

    List<Item> getAll();
}
