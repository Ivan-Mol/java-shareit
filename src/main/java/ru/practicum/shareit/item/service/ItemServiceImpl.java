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
    public ItemDto getById(long id) {
        return ItemMapper.toItemDto(itemStorage.getById(id));
    }

    @Override
    public List<ItemDto> getAll() {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item item : itemStorage.getAll()) {
            itemDto.add(ItemMapper.toItemDto(item));
        }
        return itemDto;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        userService.getById(ownerId);
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
