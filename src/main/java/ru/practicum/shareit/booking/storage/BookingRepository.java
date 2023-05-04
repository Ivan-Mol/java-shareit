package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    ArrayList<Booking> getAllByBookerIdOrderByStartDateDesc(Long bookerId);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(Long bookerId, LocalDateTime startDate);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsBeforeOrderByStartDateDesc(Long bookerId, LocalDateTime startDate);


}
