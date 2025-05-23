package com.example.newschedule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SaveUserRequestDto {

    @NotEmpty
    @Size(max = 12)
    private final String name;
    @NotEmpty
    @Email
    private final String mail;
    @NotNull
    private final String password;
}
