package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

//    @Test
//    void getAllByOwner() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("testUser");
//        user.setEmail("testuser@mail.com");
//
//        Item item = new Item();
//        item.setId(1L);
//        item.setName("testItem");
//        item.setDescription("testDescription");
//        item.setAvailable(true);
//        item.setOwner(user);
//
//        ArrayList<Item> returned = new ArrayList<>();
//        returned.add(item);
//        when(userRepository.getByIdAndCheck(1L)).thenReturn(user);
//        when(itemRepository.findByOwnerOrderByIdAsc(1L)).thenReturn(returned);
//        when(bookingRepository
//                .getAllByEndDateBeforeAndStatusAndItemInOrderByStartDateDesc(any(), any(), any())).thenReturn(new ArrayList<>());
//        List<ItemDto> actual = itemService.getAllByOwner(1L);
//        assertEquals(actual.get(0).getId(), item.getId());
//        verify(itemRepository).findByOwnerOrderByIdAsc(1L);
//        verify(userRepository).getByIdAndCheck(1L);
//    }

    @Test
    void searchAvailableItem_isValid() {
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(new User());
        ItemDto itemDto = ItemMapper.toItemDto(item);
        List<Item> returnedList = new ArrayList<>();
        returnedList.add(item);
        when(itemRepository.getItemsByQuery("testItem")).thenReturn(returnedList);

        List<ItemDto> actual = itemService.searchAvailableItem("testItem");
        assertEquals(itemDto.getId(), actual.get(0).getId());
        assertEquals(itemDto.getName(), actual.get(0).getName());
        assertEquals(itemDto.getDescription(), actual.get(0).getDescription());
    }

    @Test
    void searchAvailableItem_isBlank() {
        assertTrue(itemService.searchAvailableItem("").isEmpty());
    }

    @Test
    void createItem() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("testuser@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(user);

        when(userRepository.getByIdAndCheck(1L)).thenReturn(user);
        when(itemRepository.save(any())).thenReturn(item);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        ItemDto actual = itemService.createItem(itemDto, user.getId());
        assertEquals(actual.getId(), item.getId());
        verify(itemRepository).save(any());
    }

    @Test
    void updateItem_isValid() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("testuser@mail.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setAvailable(true);
        item.setOwner(user);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        when(itemRepository.getItemByIdAndCheck(any())).thenReturn(item);
        when(userRepository.getByIdAndCheck(any())).thenReturn(user);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto itemDto1After = itemService.updateItem(item.getId(), itemDto, user.getId());

        assertEquals(itemDto1After.getId(), itemDto.getId());
        assertEquals(itemDto1After.getName(), itemDto.getName());
        assertEquals(itemDto1After.getDescription(), itemDto.getDescription());
    }
}