package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request requestDtoToRequest(RequestDto requestDto) {
        Request request = new Request();
        request.setId(requestDto.getId());
        request.setDescription(requestDto.getDescription());
        request.setRequestorId(requestDto.getRequestorId());
        request.setCreated(requestDto.getCreated());
        return request;
    }

    public static RequestReturnDto requestToRequestReturnDto(Request request) {
        RequestReturnDto requestReturnDto = new RequestReturnDto();
        requestReturnDto.setId(request.getId());
        requestReturnDto.setDescription(request.getDescription());
        requestReturnDto.setRequestorId(request.getRequestorId());
        requestReturnDto.setCreated(request.getCreated());
        requestReturnDto.setItems(new ArrayList<>());
        return requestReturnDto;
    }

    public static List<RequestReturnDto> requestListToRequestReturnDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::requestToRequestReturnDto).collect(Collectors.toList());
    }
}
