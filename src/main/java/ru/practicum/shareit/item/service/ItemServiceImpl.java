package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto getById(Long id) {

        return ItemMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with " + id + " Id is not found")));
    }

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        User usr = UserMapper.toUser(userService.getById(ownerId));
        return itemRepository.findByOwnerId(usr.getId()).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.getItemsByQuery(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, long ownerId) {
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Item is Not Available");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("Description Is Null");
        }
        if (itemDto.getName() == null || itemDto.getName().equals("")) {
            throw new ValidationException("Name is Invalid");
        }
        User usr = UserMapper.toUser(userService.getById(ownerId));
        Item item = ItemMapper.toItem(itemDto, usr);
        item.setOwner(usr);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with " + itemId + " Id is not found"));
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
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public User getOwner(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with " + itemId + " Id is not found"));
        return item.getOwner();
    }

}
