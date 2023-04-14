package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

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
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : itemStorage.getAllByOwner(ownerId)) {
            itemDto.add(ItemMapper.toItemDto(item));
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : itemStorage.searchAvailableItem(text)) {
            itemDto.add(ItemMapper.toItemDto(item));
        }
        return itemDto;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        userService.getById(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.create(item, ownerId));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, long ownerId) {
        getById(itemId);
        userService.getById(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.update(itemId, item, ownerId));

    }
}
