package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    @NotNull
    private LocalDateTime start; //дата и время начала бронирования;
    @NotNull
    private LocalDateTime end; //дата и время конца бронирования;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
    //WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
    // REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем.
}
