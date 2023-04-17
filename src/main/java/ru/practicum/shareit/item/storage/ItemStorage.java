package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {
    Item create(Item item, User owner);

    Item update(Long itemId, Item item, User owner);

    Item getById(long id);

    List<Item> getAllByOwner(User owner);

    List<Item> searchAvailableItem(String text);
}
