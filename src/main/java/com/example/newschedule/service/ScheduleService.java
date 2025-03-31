package com.example.newschedule.service;

import com.example.newschedule.dto.ScheduleResponseDto;
import com.example.newschedule.entity.Schedule;
import com.example.newschedule.repository.ScheduleRepository;
import com.example.newschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleResponseDto saveSchedule(String title, String contents, Long userId) {
        Schedule schedule= new Schedule(title, contents);
        schedule.setUser(userRepository.findByIdOrElseThrow(userId));
        Schedule saved = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(saved);
    }

    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll().stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findById(Long id) {
        return new ScheduleResponseDto(scheduleRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    public void updateSchedule(Long id, String title, String contents, String password) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        if (!schedule.getUser().getPassword().equals(password)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}
        System.out.println(title+ " +" +  contents);
        if (!title.isEmpty()){schedule.setTitle(title);}
        if (!contents.isEmpty()){schedule.setContents(contents);}
    }

    @Transactional
    public void deleteSchedule(Long id, String password) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        if (!schedule.getUser().getPassword().equals(password)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}
        scheduleRepository.delete(schedule);
    }
}
