package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
        return ItemMapper.toItemDto(itemStorage.getById(id));
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User usr = UserMapper.toUser(userService.getById(ownerId));
        return itemStorage.getAllByOwner(usr).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        return itemStorage.searchAvailableItem(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        User usr = UserMapper.toUser(userService.getById(ownerId));
        return ItemMapper.toItemDto(itemStorage.create(item, usr));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        Item oldItem = itemStorage.getById(itemId);
        User owner = UserMapper.toUser(userService.getById(ownerId));
        if (item.getOwner() == null) {
            item.setOwner(owner);
        }
        if (!oldItem.getOwner().equals(owner)) {
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
