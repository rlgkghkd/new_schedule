package com.example.newschedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteUserRequestDto {
    @NotNull
    private String password;
}
