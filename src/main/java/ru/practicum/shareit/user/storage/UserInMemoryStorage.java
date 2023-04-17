package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class UserInMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    Long idCounter = 0L;

    @Override
    public User create(User user) {
        idCounter++;
        user.setId(idCounter);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) {
        emails.remove(users.get(user.getId()).getEmail());
        emails.add(user.getEmail());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        emails.remove(users.get(id).getEmail());
        getById(id);
        users.remove(id);
    }

    @Override
    public Set<String> getAllEmails() {
        return emails;
    }

}
