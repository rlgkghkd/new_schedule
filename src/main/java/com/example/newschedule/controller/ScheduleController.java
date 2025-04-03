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

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> saveSchedule(@RequestBody @Validated CreateScheduleRequestDto dto, HttpServletRequest request){
        return new ResponseEntity<>(scheduleService.saveSchedule(dto.getTitle(), dto.getContents(), request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll(@RequestParam(value = "index", defaultValue = "1") int index){
        return new ResponseEntity<>(scheduleService.findAll(index), HttpStatus.FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ScheduleResponseDto>> findAllByUser(@RequestParam(value = "index", defaultValue = "1") int index, HttpServletRequest request){
        return new ResponseEntity<>(scheduleService.findAllByUser(index, request), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findById(id), HttpStatus.FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateSchedule(@PathVariable Long id, @RequestBody @Validated UpdateScheduleRequestDto dto, HttpServletRequest request){
        scheduleService.updateSchedule(id, dto.getTitle(), dto.getContents(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, HttpServletRequest request){
        scheduleService.deleteSchedule(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
