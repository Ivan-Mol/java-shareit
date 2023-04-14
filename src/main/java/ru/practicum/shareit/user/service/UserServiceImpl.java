package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(userStorage.getById(id));
    }

    public UserDto create(UserDto userDto) {
        User forStorage = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.create(forStorage));
    }

    @Override
    public UserDto update(Long idFromQuery, UserDto userDto) {
        User userFromDto = UserMapper.toUser(userDto);
        User userFromStorage = userStorage.update(idFromQuery, userFromDto);
        return UserMapper.toUserDto(userFromStorage);

    }

    @Override
    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : userStorage.getAll()) {
            usersDto.add(UserMapper.toUserDto(user));
        }
        return usersDto;
    }

}
