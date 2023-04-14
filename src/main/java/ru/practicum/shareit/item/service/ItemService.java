package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Component
public interface ItemService {
    ItemDto createItem(ItemDto itemDto, long ownerId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, long ownerId);

    ItemDto getById(long id);

    List<ItemDto> getAll();
}
