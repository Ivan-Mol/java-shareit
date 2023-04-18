package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return items.values().stream().filter(i -> i.getOwner().equals(owner)).collect(Collectors.toList());

    }

    @Override
    public List<Item> searchAvailableItem(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
