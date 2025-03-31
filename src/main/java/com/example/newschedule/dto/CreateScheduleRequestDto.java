package com.example.newschedule.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateScheduleRequestDto {

    private final String title;
    private final String contents;
    private final Long userId;
}
