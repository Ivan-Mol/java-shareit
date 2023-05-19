package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceIntegrationTest {

    UserDto userDtoFromController;
    UserDto createdUserFromRepo;
    @Autowired
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userDtoFromController = new UserDto();
        userDtoFromController.setName("testName");
        userDtoFromController.setEmail("testEmail@mail.com");
        createdUserFromRepo = userService.create(userDtoFromController);
    }

    @Test
    void getById() {
        UserDto actual = userService.getById(createdUserFromRepo.getId());
        assertEquals(actual, createdUserFromRepo);
    }

    @Test
    void getById_userIdIsInvalid_throwException() {
        assertThrows(NotFoundException.class, () -> userService.getById(999L));
    }

    @Test
    void create() {
        UserDto userDto = new UserDto();
        userDto.setName("testName2");
        userDto.setEmail("testEmail2@mail.com");
        UserDto created = userService.create(userDto);
        assertEquals(userDto.getName(), created.getName());
        assertEquals(userDto.getEmail(), created.getEmail());
    }

    @Test
    void update() {
        userDtoFromController.setName("updatedName");
        userDtoFromController.setEmail("updated@Email.com");
        userDtoFromController.setId(createdUserFromRepo.getId());

        UserDto actual = userService.update(userDtoFromController);
        assertEquals(userDtoFromController, actual);

    }

    @Test
    void update_UserIdIsInvalid_throwException() {
        userDtoFromController.setName("updatedName");
        userDtoFromController.setEmail("updated@Email.com");
        userDtoFromController.setId(999L);
        assertThrows(NotFoundException.class, () -> userService.update(userDtoFromController));
    }

    @Test
    void getAll() {
        List<UserDto> list = userService.getAll();
        assertFalse(list.isEmpty());
    }

    @AfterEach
    void deleteById() {
        userService.deleteById(createdUserFromRepo.getId());
    }
}