package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemServiceIntegrationTest {
    UserDto itemOwner;
    UserDto user;
    ItemDto item;
    UserDto createdOwner;
    UserDto createdUser;
    ItemDto createdItem;
    ItemDto itemForUpdate;
    CommentDto commentDto;
    Booking booking;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        itemOwner = new UserDto();
        itemOwner.setName("testNameOwner");
        itemOwner.setEmail("testowner@mail.com");
        createdOwner = userService.create(itemOwner);

        item = new ItemDto();
        item.setName("testItem");
        item.setDescription("testItemDescription");
        item.setAvailable(true);
        createdItem = itemService.createItem(item, createdOwner.getId());

        itemForUpdate = new ItemDto();
        item.setName("updatedItemName");
        item.setDescription("updTestItemDescription");
        item.setAvailable(true);

        user = new UserDto();
        user.setName("testNameUser");
        user.setEmail("testEmailUser@mail.com");
        createdUser = userService.create(user);

        booking = new Booking();
        booking.setBooker(UserMapper.toUser(createdUser));
        booking.setItem(ItemMapper.toItem(createdItem, UserMapper.toUser(createdOwner)));
        booking.setStartDate(LocalDateTime.now().minusDays(3));
        booking.setEndDate(LocalDateTime.now().minusDays(2));
        booking.setStatus(BookingStatus.APPROVED);
        booking = bookingRepository.save(booking);
        commentDto = new CommentDto();
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setText("testText");
        commentDto.setItemId(createdItem.getId());
        commentDto.setUserId(createdUser.getId());
        commentDto.setAuthorName(createdUser.getName());
    }

    @Test
    void getById() {
        ItemDto actual = itemService.getById(createdItem.getId(), createdUser.getId());
        assertNull(actual.getLastBooking());
        assertNull(actual.getNextBooking());
    }

    @Test
    void getAllByOwner() {
        List<ItemDto> list = itemService.getAllByOwner(createdOwner.getId());
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void searchAvailableItem_isCorrect_returnList() {
        List<ItemDto> itemDtoList = itemService.searchAvailableItem("test");
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
        assertEquals(createdItem.getName(), itemDtoList.get(0).getName());
    }

    @Test
    void searchAvailableItem_isIncorrect_returnList() {
        List<ItemDto> itemDtoList = itemService.searchAvailableItem("blabla");
        assertNotNull(itemDtoList);
        assertEquals(0, itemDtoList.size());
    }

    @Test
    void update_isValid() {
        ItemDto actual = itemService.updateItem(createdItem.getId(), itemForUpdate, createdOwner.getId());
        assertEquals(actual.getName(), createdItem.getName());
        assertEquals(actual.getDescription(), createdItem.getDescription());
    }

    @Test
    void update_ownerIsInvalid() {
        assertThrows(NotFoundException.class, () -> itemService.updateItem(createdItem.getId(), createdItem, 999L));
    }

    @Test
    void createComment_isValid() {
        CommentDto actual = itemService.createComment(createdItem.getId(), commentDto, createdUser.getId());
        assertEquals(actual.getCreated().toString(), commentDto.getCreated().toString());
        assertEquals(actual.getItemId(), createdItem.getId());
        assertEquals(actual.getText(), commentDto.getText());
        assertEquals(actual.getAuthorName(), commentDto.getAuthorName());
    }

    @Test
    void createComment_isUserHasNoBookings() {
        assertThrows(ValidationException.class,
                () -> itemService.createComment(createdItem.getId(), commentDto, createdOwner.getId()));
    }

    @AfterEach
    void afterEach() {
        bookingService.deleteById(booking.getId());
        commentRepository.deleteAll();
        itemService.deleteById(createdItem.getId(), createdOwner.getId());
        userService.deleteById(createdOwner.getId());
        userService.deleteById(createdUser.getId());
    }
}