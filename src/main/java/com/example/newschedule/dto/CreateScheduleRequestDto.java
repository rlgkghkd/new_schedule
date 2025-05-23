package com.example.newschedule.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateScheduleRequestDto {

    @Size(max=20, message = "20자 이내로 작성해주세요")
    @NotEmpty(message = "제목은 공백일 수 없습니다")
    private final String title;
    @Size(max = 200, message = "200자 이내로 작성해주세요")
    private final String contents;
}
