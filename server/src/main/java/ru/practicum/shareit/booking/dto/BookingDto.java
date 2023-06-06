package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start; //дата и время начала бронирования;
    private LocalDateTime end; //дата и время конца бронирования;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
    //WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
    // REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем.
}
