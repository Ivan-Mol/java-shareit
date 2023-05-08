package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking getFirstByItemIdAndEndDateBeforeOrderByStartDateAsc(Long itemId, LocalDateTime currentTime);

    Booking getFirstByItemIdAndEndDateAfterOrderByStartDateAsc(Long itemId, LocalDateTime currentTime);

    ArrayList<Booking> getAllByItem_OwnerIdOrderByStartDateDesc(Long ownerId);

    ArrayList<Booking> getAllByItem_Owner_IdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Long ownerId, LocalDateTime currentTimeForStart, LocalDateTime currentTimeForEnd);

    ArrayList<Booking> getAllByItem_OwnerIdAndEndDateIsBeforeOrderByStartDateDesc(long ownerId, LocalDateTime now);

    ArrayList<Booking> getAllByItem_OwnerIdAndStartDateIsAfterOrderByStartDateDesc(Long ownerId, LocalDateTime currentTime);

    ArrayList<Booking> getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(Long ownerId, BookingStatus bookingStatus);

    ArrayList<Booking> getAllByBookerIdOrderByStartDateDesc(Long bookerId);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(Long bookerId, LocalDateTime currentDate);

    ArrayList<Booking> getAllByBookerIdAndEndDateIsBeforeOrderByStartDateDesc(Long bookerId, LocalDateTime currentDate);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Long bookerId, LocalDateTime currentTimeForStart, LocalDateTime currentTimeForEnd);

    ArrayList<Booking> getAllByBookerIdAndStatusOrderByStartDateDesc(Long bookerId, BookingStatus bookingStatus);
}
