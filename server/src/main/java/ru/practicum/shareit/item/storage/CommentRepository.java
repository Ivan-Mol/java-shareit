package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    ArrayList<Comment> getAllByItemId(Long itemId);
}
