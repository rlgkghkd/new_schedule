package com.example.newschedule.dto;

import com.example.newschedule.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String mail;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name= user.getName();
        this.mail= user.getMail();
    }
}
