package com.example.newschedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateUserEmailRequestDto {
    private String password;
    private String mail;
}
