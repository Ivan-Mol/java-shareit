package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String authorName;
    private LocalDateTime created;
    @NotBlank(message = "text is empty")
    private String text;
    private Long itemId;
    private Long userId;
}
