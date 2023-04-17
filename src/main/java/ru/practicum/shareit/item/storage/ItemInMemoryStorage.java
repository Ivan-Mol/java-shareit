package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemInMemoryStorage implements ItemStorage {
    Map<Long, Item> items = new HashMap<>();
    Long idCounter = 0L;

    @Override
    public Item create(Item item, User owner) {
        idCounter++;
        item.setId(idCounter);
        item.setOwner(owner);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item, User owner) {
        items.put(itemId, item);
        return items.get(itemId);
    }


    @Override
    public Item getById(long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllByOwner(User owner) {
        ArrayList<Item> ownersItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(owner)) {
                ownersItems.add(item);
            }
        }
        return ownersItems;
    }

    @Override
    public List<Item> searchAvailableItem(String text) {
        List<Item> availableItems = new ArrayList<>();
        for (Item item : items.values()) {
            String itemToString = item.toString().toLowerCase();
            if (itemToString.contains(text.toLowerCase()) && item.getAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }
}
