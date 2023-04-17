package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User create(User user);

    User getById(Long id);

    List<User> getAll();

    User update(User user);

    void deleteById(Long id);

    Set<String> getAllEmails();
}
