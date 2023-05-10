package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String authorName;
    private LocalDateTime created;
    private String text;
    private Long item_id;
    private Long user_id;
}
