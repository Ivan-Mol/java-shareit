package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerIsInvalidException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import javax.security.auth.login.AccountException;
import java.nio.file.AccessDeniedException;
import java.rmi.AccessException;
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
        if (!item.isAvailable()) {
            throw new ValidationException("Item is Not Available");
        }
        if (item.getDescription()==null){
            throw new ValidationException("Description Is Null");
        }
        if (item.getName()==null||item.getName().equals("")){
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
        if (items.get(itemId).getId() != ownerId) {
            throw new NotFoundException("");
        }
        Item oldItem = items.get(itemId);
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        boolean b = item.isAvailable();
        if (item.isAvailable() == false) {
            item.setAvailable(false);
        }
        item.setId(itemId);
        items.put(itemId,item);

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
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}
