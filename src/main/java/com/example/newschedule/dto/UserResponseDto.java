package com.example.newschedule.dto;

import com.example.newschedule.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {
    @NotNull
    private final Long id;
    @NotEmpty
    @Size(max = 12)
    private final String name;
    @NotEmpty
    @Email
    private final String mail;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name= user.getName();
        this.mail= user.getMail();
    }
}
