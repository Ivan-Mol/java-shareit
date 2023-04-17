package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailIsExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserInMemoryStorage implements UserStorage {
    Map<Long, User> users = new HashMap<>();
    Long idCounter = 0L;

    @Override
    public User create(User user) {
        if (isEmailInvalid(user.getId(), user.getEmail())) {
            throw new EmailIsExistsException("Email Is already used");
        }
        idCounter++;
        user.setId(idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("id " + id + " is not found");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(Long id, User user) {
        User userFromMap = users.get(id);
        if (user.getName() != null) {
            userFromMap.setName(user.getName());
        }
        if (isEmailInvalid(id, user.getEmail())) {
            throw new EmailIsExistsException("Email Is already exists");
        }
        if (user.getEmail() != null) {
            userFromMap.setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        users.remove(id);
    }

    @Override
    public boolean isEmailInvalid(Long id, String email) {
        List<User> list = new ArrayList<>(users.values());
        for (User user : list) {
            if (email != null && email.equals(user.getEmail()) && !user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
