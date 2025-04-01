package com.example.newschedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserPasswordRequestDto {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
