package com.example.newschedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SaveUserRequestDto {

    private final String name;
    private final String mail;
    private final String password;
}
