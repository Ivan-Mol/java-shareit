package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;
    private String name;
    @NotNull(message = "Email is null")
    @Email(message = "Email Is Invalid")
    private String email;
}