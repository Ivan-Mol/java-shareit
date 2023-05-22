package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAll() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("testuser@mail.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setName("testUser2");
        user2.setEmail("testuser2@mail.com");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> usersAfter = userService.getAll();

        assertThat(usersAfter.size()).isEqualTo(2);
    }
}