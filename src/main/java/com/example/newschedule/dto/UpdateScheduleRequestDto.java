package com.example.newschedule.dto;

import ch.qos.logback.core.util.StringUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UpdateScheduleRequestDto {

    @Size(max = 20)
    @NotEmpty
    private final String title;
    @Size(max = 200)
    @NotEmpty
    private final String contents;
    @NotNull
    private final String password;

    UpdateScheduleRequestDto(String title, String contents, String  password){
        if(StringUtil.isNullOrEmpty(title)){title = "";}
        if(StringUtil.isNullOrEmpty(contents)){contents = "";}
        this.title =title;
        this.contents = contents;
        this.password = password;
    }
}
