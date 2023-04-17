package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailIsExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            throw new EmailIsExistsException("Email is mull");
        }

        return UserMapper.toUserDto(userStorage.create(user));
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
        //return userStorage.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }



}
