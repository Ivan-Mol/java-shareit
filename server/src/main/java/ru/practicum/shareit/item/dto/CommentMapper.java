package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setCreated(comment.getCreated());
        commentDto.setText(comment.getText());
        commentDto.setItemId(comment.getItem().getId());
        commentDto.setUserId(comment.getUser().getId());
        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto, User user, Item item) {
        Comment comment = new Comment();
        comment.setCreated(commentDto.getCreated());
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setUser(user);
        return comment;
    }
}
