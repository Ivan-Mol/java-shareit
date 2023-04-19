package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Email is incorrect")
    @NotBlank(message = "Email is empty")
    private String email;
}