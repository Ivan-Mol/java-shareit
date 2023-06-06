package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @PathVariable long id) {
        log.debug("received GET /items/{}", id);
        return itemService.getById(id, ownerId);
    }

    @GetMapping
    public List<ItemDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(name = "from") Integer from,
                                       @RequestParam(name = "size") Integer size) {
        log.debug("received GET /items with HEADER {}", userId);
        return itemService.getAllByOwner(userId, from, size);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @RequestBody ItemDto itemDto) {
        log.debug("received POST /items with body {}", itemDto);
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @RequestBody ItemDto itemDto) {
        log.debug("received PATCH /items with body {}", itemDto);
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        log.debug("received DELETE /itemRequest Id/{}", itemId);
        itemService.deleteById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@RequestParam(name = "text") String text) {
        log.debug("received GET /searchAvailableItem/{}", text);
        return itemService.searchAvailableItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody CommentDto commentDto) {
        log.debug("received POST /comment with body {}", commentDto);
        return itemService.createComment(itemId, commentDto, userId);
    }
}
