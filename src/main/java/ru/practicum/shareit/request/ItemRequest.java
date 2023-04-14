package ru.practicum.shareit.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemRequest {
    private long id;
    private String description; //текст запроса, содержащий описание требуемой вещи;
    @NotNull
    private long requestorId; //пользователь, создавший запрос;
    private LocalDate created; //дата и время создания запроса;

}
