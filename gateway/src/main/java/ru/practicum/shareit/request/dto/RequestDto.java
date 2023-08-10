package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    @NotNull(message = "description is empty")
    private String description; //текст запроса, содержащий описание требуемой вещи;
    @NotNull(message = "requestorId is null")
    private long requestorId; //пользователь, создавший запрос;
    private LocalDateTime created; //дата и время создания запроса;
}
