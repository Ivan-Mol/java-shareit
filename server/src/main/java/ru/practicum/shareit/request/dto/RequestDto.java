package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private String description; //текст запроса, содержащий описание требуемой вещи;
    private long requestorId; //пользователь, создавший запрос;
    private LocalDateTime created; //дата и время создания запроса;
}
