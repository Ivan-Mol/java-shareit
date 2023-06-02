package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") Long id) {
        log.debug("received GET /items/{}", id);
        return client.getItemById(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.debug("received POST /items/ with HEADER {} and BODY {}", userId, itemDto);
        return client.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemDto itemDto,
                                         @PathVariable("id") Long id) {
        log.debug("received PATCH /items/{} with HEADER {} and BODY {}", id, userId, itemDto);
        return client.update(itemDto, id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.debug("received GET /items with HEADER {}", userId);
        return client.getAllByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam String text,
                                                      @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {
        log.debug("received GET /search with HEADER {} abd PARAM {}", userId, text);
        return client.searchAvailableItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        return client.createComment(userId, itemId, commentDto);
    }
}