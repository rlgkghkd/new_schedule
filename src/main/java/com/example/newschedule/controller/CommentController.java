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

    @PostMapping
    public ResponseEntity<CommentResponseDto> saveSchedule(@RequestBody @Validated CreateCommentRequestDto dto, HttpServletRequest request){
        return new ResponseEntity<>(commentService.saveComment(dto.getScheduleId(), dto.getContents(), request), HttpStatus.CREATED);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<List<CommentResponseDto>> findAllRelateToSchedule(@RequestParam(value = "index", defaultValue = "1") int index, @PathVariable Long scheduleId){
        return new ResponseEntity<>(commentService.findAllBySchedule(scheduleId, index), HttpStatus.FOUND);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @RequestBody @Validated UpdateCommentRequestDto dto, HttpServletRequest request){
        commentService.updateComment(id, dto.getContents(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, HttpServletRequest request){
        commentService.deleteComment(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
