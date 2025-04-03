package com.example.newschedule.service;

import com.example.newschedule.dto.ScheduleResponseDto;
import com.example.newschedule.entity.BaseEntity;
import com.example.newschedule.entity.Schedule;
import com.example.newschedule.entity.User;
import com.example.newschedule.repository.ScheduleRepository;
import com.example.newschedule.repository.UserRepository;
import com.example.newschedule.signIn.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public ScheduleResponseDto saveSchedule(String title, String contents, HttpServletRequest request) {
        Schedule schedule= new Schedule(title, contents);
        schedule.setUser(getUserByToken(request));
        Schedule saved = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(saved);
    }

    public List<ScheduleResponseDto> findAll(int index) {
        Pageable pageable = PageRequest.of(index-1, 10);
        return scheduleRepository.findAll(pageable).stream().sorted(Comparator.comparing(BaseEntity::getModifiedAt).reversed()).map(ScheduleResponseDto::new).toList();
    }

    public List<ScheduleResponseDto> findAllByUser(int index, HttpServletRequest request){
        User user = getUserByToken(request);
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        return scheduleRepository.findAllByUserOrElseThrow(user, pageRequest).stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findById(Long id) {
        return new ScheduleResponseDto(scheduleRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    public void updateSchedule(Long id, String title, String contents, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        User user = getUserByToken(request);
        if (!schedule.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}
        System.out.println(title+ " +" +  contents);
        if (!title.isEmpty()){schedule.setTitle(title);}
        if (!contents.isEmpty()){schedule.setContents(contents);}
    }

    @Transactional
    public void deleteSchedule(Long id, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        User user = getUserByToken(request);
        if (!schedule.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}
        scheduleRepository.delete(schedule);
    }

    public User getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        return userRepository.findUserByMailOrElseThrow(sub);
    }
}
