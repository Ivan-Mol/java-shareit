package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestReturnDto;

import java.util.List;

public interface RequestService {
    RequestReturnDto getById(Long requestId, Long userId);

    RequestReturnDto create(RequestDto requestDto, Long userId);

    List<RequestReturnDto> getAllByRequestor(long requestorId);

    List<RequestReturnDto> getAll(long userId, int from, int size);
}
