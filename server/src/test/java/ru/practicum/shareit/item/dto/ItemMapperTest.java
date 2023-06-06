package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ItemMapperTest {
    User user;
    Item item;
    UserDto userDto;
    ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setName("testNameUser");
        user.setEmail("test@mail.com");

        userDto = new UserDto();
        userDto.setName("testNameUser");
        userDto.setEmail("test@mail.com");

        item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("");
        item.setAvailable(true);
        item.setRequest(new Request());

        itemDto = new ItemDto();
        itemDto.setName("testItem");
        itemDto.setDescription("");
        itemDto.setAvailable(true);
    }

    @Test
    void toItemDto() {
        ItemDto actual = ItemMapper.toItemDto(item);
        assertEquals(actual.getName(), item.getName());
        assertEquals(actual.getDescription(), item.getDescription());
        assertEquals(actual.getName(), item.getName());
    }

    @Test
    void toItem() {
        Item actual = ItemMapper.toItem(itemDto, user);
        assertEquals(actual.getName(), itemDto.getName());
        assertEquals(actual.getDescription(), itemDto.getDescription());
        assertEquals(actual.getName(), itemDto.getName());
    }

    @Test
    void itemToItemShortForRequestDto() {
        ItemForRequestDto actual = ItemMapper.itemToItemShortForRequestDto(item);
        assertEquals(actual.getName(), item.getName());
        assertEquals(actual.getId(), item.getId());
        assertEquals(actual.getAvailable(), item.getAvailable());
        assertEquals(actual.getDescription(), item.getDescription());
    }

    @Test
    void itemlistToitemDtolist() {
        List<ItemDto> actual = ItemMapper.itemlistToitemDtolist(List.of(item));
        assertEquals(actual.get(0).getName(), item.getName());
        assertEquals(actual.get(0).getDescription(), item.getDescription());
        assertEquals(actual.get(0).getAvailable(), item.getAvailable());

        assertFalse(actual.isEmpty());
    }

    @Test
    void itemlistToitemForRequestDtolist() {
        List<ItemForRequestDto> actual = ItemMapper.itemlistToitemForRequestDtolist(List.of(item));
        assertEquals(actual.get(0).getName(), item.getName());
        assertEquals(actual.get(0).getDescription(), item.getDescription());
        assertEquals(actual.get(0).getAvailable(), item.getAvailable());
        assertFalse(actual.isEmpty());
    }
}