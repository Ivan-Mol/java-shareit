package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    List<UserShortDto> findUserById(Long id);

}