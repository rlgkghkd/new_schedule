package com.example.newschedule.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCommentRequestDto {

    @NotNull(message = "id는 필수입니다.")
    private final Long scheduleId;
    @Size(max = 200, message = "200자 이내로 작성해주세요")
    @NotEmpty(message = "댓글은 공백일 수 없습니다")
    private final String contents;
}
