package com.example.newschedule.dto;

import com.example.newschedule.entity.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {

    private final Long id;
    private final String contents;
    private final String commentedUser;
    private final String scheduleCommentedOn;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.commentedUser = comment.getUser().getName();
        this.scheduleCommentedOn = comment.getSchedule().getTitle();
    }
}
