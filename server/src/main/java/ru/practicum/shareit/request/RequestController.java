package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestReturnDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{requestId}")
    public RequestReturnDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long requestId) {
        log.debug("received GET /itemRequest Id/{}", requestId);
        return requestService.getById(requestId, userId);
    }

    @GetMapping
    public List<RequestReturnDto> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") long requestorId) {
        log.debug("received GET /getAllByRequestor with Id/{}", requestorId);
        return requestService.getAllByRequestor(requestorId);
    }

    @GetMapping("/all")
    public List<RequestReturnDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam Integer from,
                                         @RequestParam Integer size) {
        log.debug("received GET /All with Id/{}", userId);
        return requestService.getAll(userId, from, size);
    }

    @PostMapping
    public RequestReturnDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestBody RequestDto requestDto) {
        log.debug("received POST /itemRequest by user Id/{}", userId);
        return requestService.create(requestDto, userId);
    }

    @DeleteMapping("/{requestId}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long requestId) {
        log.debug("received DELETE / Request Id/{}", requestId);
        requestService.deleteById(requestId, userId);
    }

}
