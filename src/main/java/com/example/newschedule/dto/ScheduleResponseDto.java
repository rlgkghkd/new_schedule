package com.example.newschedule.dto;

import com.example.newschedule.entity.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String userName;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title= schedule.getTitle();
        this.contents= schedule.getContents();
        this.userName= schedule.getUser().getName();
    }
}
