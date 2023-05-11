package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(userRepository.getByIdAndCheck(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(UserDto userDto) {
        User userFromDto = UserMapper.toUser(userDto);
        User userFromStorage = userRepository.getByIdAndCheck(userFromDto.getId());
        if (userFromDto.getName() == null) {
            userFromDto.setName(userFromStorage.getName());
        }
        if (userFromDto.getEmail() == null) {
            userFromDto.setEmail(userFromStorage.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(userFromDto));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

}
