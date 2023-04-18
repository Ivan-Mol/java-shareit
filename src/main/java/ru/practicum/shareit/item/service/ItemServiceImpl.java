package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto getById(Long id) {
        if (itemStorage.getById(id) == null) {
            throw new NotFoundException("Item with " + id + " Id is not found");
        }
        return ItemMapper.toItemDto(itemStorage.getById(id));
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User usr = UserMapper.toUser(userService.getById(ownerId));
        return itemStorage.getAllByOwner(usr).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.searchAvailableItem(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        User usr = UserMapper.toUser(userService.getById(ownerId));
        Item item = ItemMapper.toItem(itemDto, usr);
        if (item.getAvailable() == null) {
            throw new ValidationException("Item is Not Available");
        }
        if (item.getDescription() == null) {
            throw new ValidationException("Description Is Null");
        }
        if (item.getName() == null || item.getName().equals("")) {
            throw new ValidationException("Name is Invalid");
        }
        return ItemMapper.toItemDto(itemStorage.create(item, usr));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item oldItem = itemStorage.getById(itemId);
        User owner = UserMapper.toUser(userService.getById(ownerId));
        Item item = ItemMapper.toItem(itemDto, owner);
        if (item.getOwner() == null) {
            item.setOwner(owner);
        }
        if (!oldItem.getOwner().getId().equals(ownerId)) {
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
        return ItemMapper.toItemDto(itemStorage.update(itemId, item, owner));

    }
}
