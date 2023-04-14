package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long idFromQuery, UserDto userDto);

    void deleteUserById(Long id);
}
