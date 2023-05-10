package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    default Booking getByIdAndCheck(Long id){
       return findById(id).orElseThrow(() -> new NotFoundException("Booking with " + id + " Id is not found"));
    }
    Booking getFirstByItemIdAndStatusAndStartDateLessThanEqualOrderByStartDateDesc(Long itemId, BookingStatus bookingStatus, LocalDateTime currentTime);

    Booking getFirstByItemIdAndStatusAndStartDateIsAfterOrderByStartDateAsc(Long itemId, BookingStatus bookingStatus, LocalDateTime currentTime);

    ArrayList<Booking> getAllByEndDateBeforeAndStatusAndItemInOrderByStartDateDesc(LocalDateTime currentTime, BookingStatus bookingStatus, List<Item> items);

    ArrayList<Booking> getAllByStartDateAfterAndStatusAndItemInOrderByStartDateDesc(LocalDateTime currentTime, BookingStatus bookingStatus, List<Item> items);

    ArrayList<Booking> getAllByItem_OwnerIdOrderByStartDateDesc(Long ownerId);

    ArrayList<Booking> getAllByItem_Owner_IdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Long ownerId, LocalDateTime currentTimeForStart, LocalDateTime currentTimeForEnd);

    ArrayList<Booking> getAllByItem_OwnerIdAndEndDateIsBeforeOrderByStartDateDesc(long ownerId, LocalDateTime now);

    ArrayList<Booking> getAllByItem_OwnerIdAndStartDateIsAfterOrderByStartDateDesc(Long ownerId, LocalDateTime currentTime);

    ArrayList<Booking> getAllByItem_OwnerIdAndStatusOrderByStartDateDesc(Long ownerId, BookingStatus bookingStatus);

    ArrayList<Booking> getAllByBookerIdOrderByStartDateDesc(Long bookerId);

    ArrayList<Booking> getAllByBookerIdAndItem_IdAndStatusAndEndDateIsBefore(Long bookerId, Long itemId, BookingStatus bookingStatus, LocalDateTime currentTime);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsAfterOrderByStartDateDesc(Long bookerId, LocalDateTime currentDate);

    ArrayList<Booking> getAllByBookerIdAndEndDateIsBeforeOrderByStartDateDesc(Long bookerId, LocalDateTime currentDate);

    ArrayList<Booking> getAllByBookerIdAndStartDateIsBeforeAndEndDateIsAfterOrderByStartDateDesc(Long bookerId, LocalDateTime currentTimeForStart, LocalDateTime currentTimeForEnd);

    ArrayList<Booking> getAllByBookerIdAndStatusOrderByStartDateDesc(Long bookerId, BookingStatus bookingStatus);
}
