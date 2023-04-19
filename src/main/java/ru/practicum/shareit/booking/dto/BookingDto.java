package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class BookingDto {
    private long id;
    private LocalDate start; //дата и время начала бронирования;
    private LocalDate end; //дата и время конца бронирования;
    @NotNull
    private long itemId; //вещь, которую пользователь бронирует;
    @NotNull
    private long bookerId; //пользователь, который осуществляет бронирование;
    private BookingStatus status; //WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
    // REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем.
}
