package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingReturnDto {
    private Long id;
    @NotNull
    private LocalDateTime start; //дата и время начала бронирования;
    @NotNull
    private LocalDateTime end; //дата и время конца бронирования;

    private ItemShortDto item; //вещь, которую пользователь бронирует;
    private UserShortDto booker; //пользователь, который осуществляет бронирование;
    private BookingStatus status; //WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
    // REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем.
}
