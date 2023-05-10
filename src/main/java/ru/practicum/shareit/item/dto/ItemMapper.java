package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getRequest());
        itemDto.setComments(new ArrayList<>());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemDto.getRequest());
        item.setOwner(owner);
        return item;
    }

    public static List<ItemDto> itemlistToitemdtolist(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
