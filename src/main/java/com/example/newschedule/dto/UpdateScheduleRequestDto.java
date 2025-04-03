package com.example.newschedule.dto;

import ch.qos.logback.core.util.StringUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateScheduleRequestDto {

    @Size(max = 20)
    private final String title;
    @Size(max = 200)
    private final String contents;

    UpdateScheduleRequestDto(String title, String contents, String  password){
        if(StringUtil.isNullOrEmpty(title)){title = "";}
        if(StringUtil.isNullOrEmpty(contents)){contents = "";}
        this.title =title;
        this.contents = contents;
    }
}
