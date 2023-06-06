package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDate; //дата и время начала бронирования;
    private LocalDateTime endDate; //дата и время конца бронирования;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; //вещь, которую пользователь бронирует;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker; //пользователь, который осуществляет бронирование;
    @Enumerated(EnumType.STRING)
    private BookingStatus status; //WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем,
    // REJECTED — бронирование отклонено владельцем, CANCELED — бронирование отменено создателем.
}
