package com.example.newschedule.dto;

import com.example.newschedule.entity.Schedule;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleResponseDto {

    @NotNull
    private final Long id;
    @NotEmpty
    @Size(max = 20)
    private final String title;
    @NotEmpty
    @Size(max = 200)
    private final String contents;
    @NotEmpty
    @Size(max = 12)
    private final String userName;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title= schedule.getTitle();
        this.contents= schedule.getContents();
        this.userName= schedule.getUser().getName();
    }
}
