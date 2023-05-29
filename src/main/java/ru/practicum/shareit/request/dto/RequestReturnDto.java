package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestReturnDto {
    private Long id;
    private String description; //текст запроса, содержащий описание требуемой вещи;
    private long requestorId; //пользователь, создавший запрос;
    private LocalDateTime created; //дата и время создания запроса;
    private List<ItemForRequestDto> items;
}

