package com.example.newschedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserPasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}
