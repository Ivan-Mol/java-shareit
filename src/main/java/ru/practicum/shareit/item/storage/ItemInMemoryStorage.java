package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemInMemoryStorage implements ItemStorage {
    Map<Long, Item> items = new HashMap<>();
    Long idCounter = 0L;

    @Override
    public Item create(Item item, Long ownerId) {
        if (item.getAvailable() == null) {
            throw new ValidationException("Item is Not Available");
        }
        if (item.getDescription() == null) {
            throw new ValidationException("Description Is Null");
        }
        if (item.getName() == null || item.getName().equals("")) {
            throw new ValidationException("Name is Invalid");
        }
        idCounter++;
        item.setId(idCounter);
        item.setOwnerId(ownerId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long itemId, Item item, long ownerId) {
        Item oldItem = items.get(itemId);
        if (item.getOwnerId() == null) {
            item.setOwnerId(ownerId);
        }
        if (oldItem.getOwnerId() != ownerId) {
            throw new NotFoundException("User is incorrect");
        }
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        item.setId(itemId);
        items.put(itemId, item);

        return items.get(itemId);
    }


    @Override
    public Item getById(long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with " + id + " Id is not found");
        }
        return items.get(id);
    }

    @Override
    public List<Item> getAllByOwner(Long ownerId) {
        ArrayList<Item> ownersItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId().equals(ownerId)) {
                ownersItems.add(item);
            }
        }
        return ownersItems;
    }

    @Override
    public List<Item> searchAvailableItem(String text) {
        if (text == null || text.equals("")) {
            return new ArrayList<>();
        }
        List<Item> availableItems = new ArrayList<>();
        for (Item item : items.values()) {
            String itemToString = item.toString().toLowerCase();
            if (itemToString.contains(text.toLowerCase()) & item.getAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }
}
