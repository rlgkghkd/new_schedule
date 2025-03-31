package com.example.newschedule.dto;

import ch.qos.logback.core.util.StringUtil;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UpdateScheduleRequestDto {

    private final String title;
    private final String contents;
    private final String password;

    UpdateScheduleRequestDto(String title, String contents, String  password){
        if(StringUtil.isNullOrEmpty(title)){title = "";}
        if(StringUtil.isNullOrEmpty(contents)){contents = "";}
        this.title =title;
        this.contents = contents;
        this.password = password;
    }
}
