package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailIsExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto getById(Long id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException("id " + id + " is not found");
        }
        return UserMapper.toUserDto(userStorage.getById(id));
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            throw new EmailIsExistsException("Email is mull");
        }
        if (isEmailInvalid(user.getId(), user.getEmail())) {
            throw new EmailIsExistsException("Email Is already used");
        }
        return UserMapper.toUserDto(userStorage.create(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        User userFromDto = UserMapper.toUser(userDto);
        User userFromStorage = userStorage.getById(userFromDto.getId());
        if (userFromDto.getName() == null) {
            userFromDto.setName(userFromStorage.getName());
        }
        if (isEmailInvalid(userFromDto.getId(), userFromDto.getEmail())) {
            throw new EmailIsExistsException("Email Is already used");
        }
        if (userFromDto.getEmail() == null) {
            userFromDto.setEmail(userFromStorage.getEmail());
        }
        return UserMapper.toUserDto(userStorage.update(userFromDto));
    }

    @Override
    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public boolean isEmailInvalid(Long id, String email) {
        Set<String> emails = userStorage.getAllEmails();
        if (emails.contains(email) && !userStorage.getById(id).getEmail().equals(email)) {
            throw new EmailIsExistsException("Email Is already used");

        }
        return false;
    }

}
