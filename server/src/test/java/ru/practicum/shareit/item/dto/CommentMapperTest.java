package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentMapperTest {
    User user;
    CommentDto commentDto;
    Comment comment;
    Item item;

    @BeforeEach
    void beforeEach() {
        LocalDateTime currentTime = LocalDateTime.now();

        user = new User();
        user.setId(1L);
        user.setName("testNameUser");
        user.setEmail("test@mail.com");

        item = new Item();
        item.setName("testItem");
        item.setDescription("");
        item.setAvailable(true);

        comment = new Comment();
        comment.setId(1L);
        comment.setUser(user);
        comment.setCreated(currentTime);
        comment.setItem(item);
        comment.setText("testCommentText");

        commentDto = new CommentDto();
        commentDto.setUserId(user.getId());
        commentDto.setCreated(currentTime);
        commentDto.setItemId(item.getId());
        commentDto.setText("testCommentText");
    }

    @Test
    void toCommentDto() {
        CommentDto actual = CommentMapper.toCommentDto(comment);
        assertEquals(actual.getId(), comment.getId());
        assertEquals(actual.getAuthorName(), comment.getUser().getName());
        assertEquals(actual.getCreated().toString(), comment.getCreated().toString());
        assertEquals(actual.getText(), comment.getText());
        assertEquals(actual.getItemId(), comment.getItem().getId());
        assertEquals(actual.getUserId(), comment.getUser().getId());
    }

    @Test
    void toComment() {
        Comment actual = CommentMapper.toComment(commentDto, user, item);
        assertEquals(actual.getUser(), user);
        assertEquals(actual.getItem(), item);
        assertEquals(actual.getText(), commentDto.getText());
        assertEquals(actual.getCreated().toString(), commentDto.getCreated().toString());
    }
}