package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    default User getByIdAndCheck(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User with " + id + " Id is not found"));
    }
}