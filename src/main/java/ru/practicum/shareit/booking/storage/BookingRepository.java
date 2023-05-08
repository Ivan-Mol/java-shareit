package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    ArrayList<Booking> getAllByEndDateBeforeAndStatusAndItemIn(LocalDateTime currentTime,BookingStatus bookingStatus, List<Item> items);
    ArrayList<Booking> getAllByStartDateAfterAndStatusAndItemIn(LocalDateTime currentTime,BookingStatus bookingStatus, List<Item> items);

    Booking getFirstByItemIdAndEndDateBeforeAndStatusOrderByStartDateAsc(Long itemId, LocalDateTime currentTime,BookingStatus bookingStatus);

    Booking getFirstByItemIdAndStartDateAfterAndStatusOrderByStartDateAsc(Long itemId, LocalDateTime currentTime,BookingStatus bookingStatus);

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
