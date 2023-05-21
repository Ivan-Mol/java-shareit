package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookingServiceIntegrationTest {

    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    BookingDto bookingFromController;
    BookingDto bookingWithUnavailableItem;
    BookingResponseDto createdBooking;
    UserDto createdOwner;
    UserDto createdBooker2;
    UserDto createdBooker;
    ItemDto createdItem;
    ItemDto createdItem2;

    @BeforeEach
    void beforeEach() {
        UserDto user = new UserDto();
        user.setName("testNameUser");
        user.setEmail("test@mail.com");
        createdOwner = userService.create(user);

        UserDto user2 = new UserDto();
        user2.setName("testNameUser2");
        user2.setEmail("test22@mail.com");
        createdBooker2 = userService.create(user2);

        UserDto booker = new UserDto();
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");
        createdBooker = userService.create(booker);

        ItemDto item = new ItemDto();
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        createdItem = itemService.createItem(item, createdOwner.getId());

        ItemDto item2 = new ItemDto();
        item2.setName("testItem");
        item2.setDescription("testDescription");
        item2.setAvailable(false);
        createdItem2 = itemService.createItem(item2, createdOwner.getId());

        LocalDateTime currentTime = LocalDateTime.now();

        bookingFromController = new BookingDto();
        bookingFromController.setItemId(createdItem.getId());
        bookingFromController.setStatus(BookingStatus.WAITING);
        bookingFromController.setStart(currentTime.plusDays(1));
        bookingFromController.setEnd(currentTime.plusDays(2));
        createdBooking = bookingService.create(bookingFromController, createdBooker.getId());

        bookingWithUnavailableItem = new BookingDto();
        bookingWithUnavailableItem.setItemId(createdItem2.getId());
        bookingWithUnavailableItem.setStart(currentTime.plusDays(2));
        bookingWithUnavailableItem.setEnd(currentTime.plusDays(3));
    }

    @Test
    void create_itemIsInvalid() {
        assertThrows(ValidationException.class, () -> bookingService.create(bookingWithUnavailableItem, createdBooker2.getId()));
    }

    @Test
    void create_BookerIsOwner() {
        assertThrows(NotFoundException.class, () -> bookingService.create(bookingWithUnavailableItem, createdOwner.getId()));
    }

    @Test
    void getById() {
        BookingResponseDto actual = bookingService.getById(createdBooking.getId(), createdOwner.getId());

        assertNotNull(actual);
        assertEquals(createdBooking.getItem().getId(), actual.getItem().getId());
        assertEquals(createdBooking.getStart().withNano(0), actual.getStart().withNano(0));
        assertEquals(createdBooking.getEnd().withNano(0), actual.getEnd().withNano(0));
    }

    @Test
    void getAllByOwner() {
        List<BookingResponseDto> bookings = bookingService.getAllByOwner(createdOwner.getId(),
                "WAITING", 0, 10);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(createdBooker.getId(), bookings.get(0).getBooker().getId());
        assertEquals(bookingFromController.getStatus(), bookings.get(0).getStatus());
    }

    @Test
    void getAllByBooker() {
        List<BookingResponseDto> bookings = bookingService.getAllByBooker(createdBooker.getId(),
                "WAITING", 0, 10);

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(createdBooker.getId(), bookings.get(0).getBooker().getId());
        assertEquals(bookingFromController.getStatus(), bookings.get(0).getStatus());
    }

    @Test
    void getAllByBooker_isUserIncorrect() {
        assertThrows(NotFoundException.class, () ->
                bookingService.getAllByBooker(999L, BookingStatus.WAITING.toString(), 0, 10));
    }

    @AfterEach
    void afterEach() {
        bookingService.deleteById(createdBooking.getId());
        itemService.deleteById(createdItem.getId(), createdOwner.getId());
        itemService.deleteById(createdItem2.getId(), createdOwner.getId());
        userService.deleteById(createdOwner.getId());
        userService.deleteById(createdBooker.getId());
        userService.deleteById(createdBooker2.getId());

    }
}