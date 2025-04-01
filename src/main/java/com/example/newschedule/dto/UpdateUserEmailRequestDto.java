package com.example.newschedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserEmailRequestDto {
    @NotNull
    private String password;
    @NotEmpty
    @Email
    private String mail;
}
