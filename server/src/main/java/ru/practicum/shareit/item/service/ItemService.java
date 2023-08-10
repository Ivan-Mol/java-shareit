package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Component
public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId);

    ItemDto getById(Long id, Long ownerId);

    List<ItemDto> getAllByOwner(Long userId, Integer from, Integer size);

    List<ItemDto> searchAvailableItem(String text);

    CommentDto createComment(Long itemId, CommentDto commentDto, Long ownerId);

    void deleteById(Long itemId, Long userId);
}
