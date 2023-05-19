package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ItemServiceIntegrationTest {
    UserDto itemOwner;
    UserDto user;
    ItemDto item;
    UserDto createdOwner;
    UserDto createdUser;
    ItemDto createdItem;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

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

        user = new UserDto();
        user.setName("testNameUser");
        user.setEmail("testEmailUser@mail.com");
        createdUser = userService.create(user);

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
        assertEquals(item.getName(), itemDtoList.get(0).getName());
    }

    @Test
    void searchAvailableItem_isIncorrect_returnList() {
        List<ItemDto> itemDtoList = itemService.searchAvailableItem("blabla");
        assertNotNull(itemDtoList);
        assertEquals(0, itemDtoList.size());
    }

    @AfterEach
    void afterEach() {
        itemService.deleteById(createdItem.getId(), createdOwner.getId());
        userService.deleteById(createdOwner.getId());
        userService.deleteById(createdUser.getId());
    }
}