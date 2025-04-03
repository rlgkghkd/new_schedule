package com.example.newschedule.controller;

import com.example.newschedule.dto.CreateScheduleRequestDto;
import com.example.newschedule.dto.ScheduleResponseDto;
import com.example.newschedule.dto.UpdateScheduleRequestDto;
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
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    //모든 요청은 jwt 토큰을 필요로 함.
    
    //스케줄 생성
    //dto 제목, 본문
    //jwt 토큰으로 현재 로그인 유저 검색
    //생성된 스케줄은 자동으로 로그인 한 유저의 소유가 됨
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@RequestBody @Validated CreateScheduleRequestDto dto, HttpServletRequest request){
        return new ResponseEntity<>(scheduleService.saveSchedule(dto.getTitle(), dto.getContents(), request), HttpStatus.CREATED);
    }

    //스케줄 전부 검색
    //페이징 크기는 10, 파라미터로 페이지 인덱스
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(@RequestParam(value = "index", defaultValue = "1") int index){
        return new ResponseEntity<>(scheduleService.findAll(index), HttpStatus.FOUND);
    }

    //로그인 한 유저 소유의 스케줄 전부 검색
    //페이징 크기는 10, 파라미터로 페이지 인덱스
    @GetMapping("/user")
    public ResponseEntity<List<ScheduleResponseDto>> findAllByUser(@RequestParam(value = "index", defaultValue = "1") int index, HttpServletRequest request){
        return new ResponseEntity<>(scheduleService.findAllByUser(index, request), HttpStatus.FOUND);
    }

    //스케줄 id 기반 검색
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.FOUND);
    }

    //스케줄 수정
    //수정할 스케줄은 경로변수로 탐색
    //스케줄 소유주가 로그인 한 유저와 일치할 경우만 수정 가능
    //스케줄은 제목, 본문을 각각 수정 가능. 수정하고 싶지 않은 항목은 요청에서 생략 가능
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Long id, @RequestBody @Validated UpdateScheduleRequestDto dto, HttpServletRequest request){
        scheduleService.updateSchedule(id, dto.getTitle(), dto.getContents(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //스케줄 삭제
    //삭제할 스케줄은 경로변수로 탐색
    //스케줄 소유주가 로그인 한 유저와 일치할 경우만 삭제 가능
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpServletRequest request){
        scheduleService.deleteSchedule(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
