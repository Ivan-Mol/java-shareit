package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto update(Long idFromQuery, UserDto userDto);

    void deleteById(Long id);

}
