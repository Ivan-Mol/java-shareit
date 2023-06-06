package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotNull(groups = Update.class)
    private Long id;
    private String name;
    @NotNull(message = "Email is null", groups = Create.class)
    @Email(message = "Email Is Invalid", groups = {Update.class, Create.class})
    private String email;
}