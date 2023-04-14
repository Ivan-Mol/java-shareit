package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Email is incorrect")
    @NotBlank(message = "Email is empty")
    private String email;
}