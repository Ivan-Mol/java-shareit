package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User getById(Long id);

    List<User> getAll();

    User update(Long idFromQuery, User user);

    void deleteById(Long id);

    boolean isEmailInvalid(Long id, String email);
}
