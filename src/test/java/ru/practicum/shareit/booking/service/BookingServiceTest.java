package ru.practicum.shareit.booking.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getById() {
    }

    @Test
    void create_isValid() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(itemRepository.getByIdAndCheck(any())).thenReturn(item);
        when(userRepository.getByIdAndCheck(any())).thenReturn(booker);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponseDto bookingDtoForReturn = bookingService.create(BookingMapper.toBookingDto(booking), booker.getId());
        assertEquals(bookingDtoForReturn.getId(), booking.getId());
        assertEquals(bookingDtoForReturn.getBooker().getId(), booking.getBooker().getId());
        assertEquals(bookingDtoForReturn.getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoForReturn.getStart(), booking.getStartDate());
        assertEquals(bookingDtoForReturn.getEnd(), booking.getEndDate());

        verify(itemRepository).getByIdAndCheck(any());
        verify(userRepository).getByIdAndCheck(any());
        verify(bookingRepository).save(any());
    }

    @Test
    void create_BookerIsOwner() {
        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(booker);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(itemRepository.getByIdAndCheck(any())).thenReturn(item);
        when(userRepository.getByIdAndCheck(any())).thenReturn(booker);

        assertThrows(NotFoundException.class, () -> bookingService.create(BookingMapper.toBookingDto(booking), booker.getId()));
        verify(itemRepository).getByIdAndCheck(any());
        verify(userRepository).getByIdAndCheck(any());
    }

    @Test
    void create_ItemIsUnavailable() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(false);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(itemRepository.getByIdAndCheck(any())).thenReturn(item);
        when(userRepository.getByIdAndCheck(any())).thenReturn(booker);


        assertThrows(ValidationException.class, () -> bookingService.create(BookingMapper.toBookingDto(booking), booker.getId()));
        verify(itemRepository).getByIdAndCheck(any());
        verify(userRepository).getByIdAndCheck(any());
    }

    @Test
    void approvingByOwner_isApproved() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(bookingRepository.getByIdAndCheck(1L)).thenReturn(booking);
        when(userRepository.getByIdAndCheck(2L)).thenReturn(ownerOfItem);
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponseDto bookingResponseDto = bookingService.approvingByOwner(1L, 2L, true);
        assertEquals(bookingResponseDto.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void approvingByOwner_isAlreadyApproved() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(bookingRepository.getByIdAndCheck(1L)).thenReturn(booking);
        when(userRepository.getByIdAndCheck(2L)).thenReturn(ownerOfItem);
        assertThrows(ValidationException.class, () -> bookingService.approvingByOwner(1L, 2L, true));
    }

    @Test
    void approvingByOwner_isStatusNull() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(bookingRepository.getByIdAndCheck(1L)).thenReturn(booking);
        when(userRepository.getByIdAndCheck(2L)).thenReturn(ownerOfItem);
        assertThrows(ValidationException.class, () -> bookingService.approvingByOwner(1L, 2L, null));
    }

    @Test
    void getAllByOwner_ALL() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        when(userRepository.getByIdAndCheck(2L)).thenReturn(ownerOfItem);
        ArrayList<Booking> bookingsForResponse = new ArrayList<>();
        bookingsForResponse.add(booking);
        when(bookingRepository.getAllByItem_OwnerIdOrderByStartDateDesc(any(), any())).thenReturn(bookingsForResponse);

        List<BookingResponseDto> bookingResponseDtoList = bookingService.getAllByOwner(ownerOfItem.getId(), "ALL", 0, 10);
        assertFalse(bookingResponseDtoList.isEmpty());
        assertEquals(bookingResponseDtoList.get(0).getId(), booking.getId());
        verify(bookingRepository).getAllByItem_OwnerIdOrderByStartDateDesc(any(), any());
    }

    @Test
    void getAllByOwner_UNSUPPORTED_STATUS() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        assertThrows(ValidationException.class, () -> bookingService.getAllByOwner(ownerOfItem.getId(), "blabla", 0, 10));
    }

    @Test
    void getAllByBooker() {
        User ownerOfItem = new User();
        ownerOfItem.setId(2L);
        ownerOfItem.setName("testNameUser2");
        ownerOfItem.setEmail("test22@mail.com");

        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(ownerOfItem);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().plusHours(1));
        booking.setEndDate(LocalDateTime.now().plusDays(1));

        ArrayList<Booking> bookingsForResponse = new ArrayList<>();
        bookingsForResponse.add(booking);
        when(bookingRepository.getAllByBookerIdOrderByStartDateDesc(any(), any())).thenReturn(bookingsForResponse);
        List<BookingResponseDto> bookingResponseDtoList = bookingService.getAllByBooker(1L, "ALL", 0, 10);
        assertFalse(bookingResponseDtoList.isEmpty());
        assertEquals(bookingResponseDtoList.get(0).getId(), booking.getId());
        verify(bookingRepository).getAllByBookerIdOrderByStartDateDesc(any(), any());
    }

    @Test
    void getAllByBooker_UNSUPPORTED_STATUS() {
        User booker = new User();
        booker.setId(1L);
        booker.setName("testBooker");
        booker.setEmail("testbooker@mail.com");

        assertThrows(ValidationException.class, () -> bookingService.getAllByBooker(booker.getId(), "blabla", 0, 10));
    }
}