package com.example.newschedule.controller;

import com.example.newschedule.dto.*;
import com.example.newschedule.service.CommentService;
import com.example.newschedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    //모든 요청은 jwt 토큰을 필요로 함.

    //댓글 생성
    //dto 스케줄id, 댓글 본문
    //댓글 소유주는 현재 로그인 한 유저
    @PostMapping
    public ResponseEntity<CommentResponseDto> saveSchedule(@RequestBody @Validated CreateCommentRequestDto dto, HttpServletRequest request){
        return new ResponseEntity<>(commentService.saveComment(dto.getScheduleId(), dto.getContents(), request), HttpStatus.CREATED);
    }

    //스케줄에 달린 모든 댓글 검색
    //페이지 크기는 10, 파라미터로 index 넘겨줌
    @GetMapping("/{scheduleId}")
    public ResponseEntity<List<CommentResponseDto>> findAllRelateToSchedule(@RequestParam(value = "index", defaultValue = "1") int index, @PathVariable Long scheduleId){
        return new ResponseEntity<>(commentService.findAllBySchedule(scheduleId, index), HttpStatus.FOUND);
    }

    //댓글 수정
    //댓글 id로 수정할 댓글 탐색
    //dto 수정할 본문 내용
    //댓글 소유 유저와 로그인 한 유저가 일치하면 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @RequestBody @Validated UpdateCommentRequestDto dto, HttpServletRequest request){
        commentService.updateComment(id, dto.getContents(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //댓글 삭제
    //댓글 id로 삭제할 댓글 탐색
    //댓글 소유 유저와 로그인 한 유저가 일치하면 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, HttpServletRequest request){
        commentService.deleteComment(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
