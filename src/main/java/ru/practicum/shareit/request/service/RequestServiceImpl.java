package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestReturnDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestReturnDto getById(Long requestId, Long userId) {
        userRepository.getByIdAndCheck(userId);
        Request request = requestRepository.getByIdAndCheck(requestId);
        List<Item> items = itemRepository.getItemsByRequest(request);
        RequestReturnDto requestReturnDto = RequestMapper.requestToRequestReturnDto(request);
        requestReturnDto.setItems(ItemMapper.itemlistToitemForRequestDtolist(items));
        return requestReturnDto;
    }

    @Override
    public RequestReturnDto create(RequestDto requestDto, Long userId) {
        userRepository.getByIdAndCheck(userId);
        requestDto.setRequestorId(userId);
        requestDto.setCreated(LocalDateTime.now());
        Request request = requestRepository.save(RequestMapper.requestDtoToRequest(requestDto));
        return RequestMapper.requestToRequestReturnDto(request);
    }

    @Override
    public List<RequestReturnDto> getAllByRequestor(long requestorId) {
        userRepository.getByIdAndCheck(requestorId);
        List<Request> requests = requestRepository.getAllByRequestorId(requestorId);
        return getRequestReturnDtoWithItems(requests);
    }

    @Override
    public List<RequestReturnDto> getAll(long userId, int from, int size) {
        userRepository.getByIdAndCheck(userId);
        List<Request> requests = requestRepository.findRequestsByRequestorIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size));
        return getRequestReturnDtoWithItems(requests);
    }

    @Override
    public void deleteById(Long requestId, Long userId) {
        userRepository.getByIdAndCheck(userId);
        requestRepository.deleteById(requestId);
    }

    public List<RequestReturnDto> getRequestReturnDtoWithItems(List<Request> requests) {
        List<RequestReturnDto> requestReturnDtos = RequestMapper.requestListToRequestReturnDtoList(requests);
        List<Item> itemsWithRequestsId = itemRepository.getItemsByRequestIn(requests);
        for (RequestReturnDto request : requestReturnDtos) {
            for (Item item : itemsWithRequestsId) {
                if (request.getId().equals(item.getRequest().getId())) {
                    request.getItems().add(ItemMapper.itemToItemShortForRequestDto(item));
                }
            }
        }
        return requestReturnDtos;
    }
}
