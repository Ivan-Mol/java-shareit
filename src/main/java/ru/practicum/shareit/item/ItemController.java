package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id) {
        log.debug("received GET /items/{}", id);
        return itemService.getById(id);
    }
    @GetMapping
    public List<ItemDto> getAll() {
        log.debug("received GET /items/");
        return itemService.getAll();
    }
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody @Valid ItemDto itemDto) {
        log.debug("received POST /items with body {}", itemDto);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") long ownerId,
                          @RequestBody @Valid ItemDto itemDto) {
        log.debug("received PATCH /items with body {}", itemDto);
        return itemService.updateItem(itemId, itemDto, ownerId);
    }
}
