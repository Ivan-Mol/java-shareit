package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestReturnDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class RequestServiceImplTest {
    UserDto userDto;

    UserDto createdUser;
    RequestDto requestDtoFromController;

    RequestReturnDto createdRequestReturnDto;
    @Autowired
    RequestService requestService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto();
        userDto.setName("testName");
        userDto.setEmail("testEmail@mail.com");
        createdUser = userService.create(userDto);

        requestDtoFromController = new RequestDto();
        requestDtoFromController.setDescription("testDescription");
        requestDtoFromController.setRequestorId(createdUser.getId());
        createdRequestReturnDto = requestService.create(requestDtoFromController, createdUser.getId());

    }

    @Test
    void getById() {
        RequestReturnDto actual = requestService.getById(createdRequestReturnDto.getId(), createdUser.getId());

        assertNotNull(actual);
        assertEquals(createdRequestReturnDto.getId(), actual.getId());
        assertEquals(createdRequestReturnDto.getDescription(), actual.getDescription());
        assertEquals(createdRequestReturnDto.getCreated().withNano(0), actual.getCreated().withNano(0));
    }

    @Test
    void getById_isUserInvalid_Exception() {
        assertThrows(NotFoundException.class, () -> requestService.getById(createdRequestReturnDto.getId(), 999L));
    }

    @Test
    void getAllByRequestor() {
        List<RequestReturnDto> actualList = requestService.getAllByRequestor(createdUser.getId());
        assertFalse(actualList.isEmpty());
        assertEquals(actualList.get(0).getRequestorId(), createdUser.getId());
    }

    @Test
    void getAll() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("testName2");
        userDto1.setEmail("testMail@mail.com");
        UserDto actualUser = userService.create(userDto1);
        List<RequestReturnDto> actualList = requestService.getAll(actualUser.getId(), 0, 99);
        assertFalse(actualList.isEmpty());
        userService.deleteById(actualUser.getId());
    }

    @AfterEach
    void afterEach() {
        requestService.deleteById(createdRequestReturnDto.getId(), createdUser.getId());
        userService.deleteById(createdUser.getId());
    }
}