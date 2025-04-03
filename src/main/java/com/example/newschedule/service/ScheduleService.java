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


    //스케줄 생성
    //스케줄 소유주는 jwt토큰에서 찾음
    public ScheduleResponseDto saveSchedule(String title, String contents, HttpServletRequest request) {
        Schedule schedule= new Schedule(title, contents);
        schedule.setUser(getUserByToken(request));
        Schedule saved = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(saved);
    }

    //스케줄 전체 검색
    //index에서 -1
    //Pageable 객체를 findAll 메서드에 넘김
    //반환받는 객체는 Page<schedule>객체, dto로 맵핑하고 리스트로 전환함
    public List<ScheduleResponseDto> findAll(int index) {
        Pageable pageable = PageRequest.of(index-1, 10);
        return scheduleRepository.findAll(pageable).stream().sorted(Comparator.comparing(BaseEntity::getModifiedAt).reversed()).map(ScheduleResponseDto::new).toList();
    }

    //특정 유저가 소유한 모든 스케줄 검색
    //findAll이 아닌 검색방법은 PageRequest 객체를 이용해 수동으로 페이징 해야 함.
    //반환받는 객체는 PageImpl<Schedule>, dto로 맵핑, 리스트로 전환
    public List<ScheduleResponseDto> findAllByUser(int index, HttpServletRequest request){
        User user = getUserByToken(request);
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        return scheduleRepository.findAllByUserOrElseThrow(user, pageRequest).stream().map(ScheduleResponseDto::new).toList();
    }

    //스케줄 id로 스케줄 검색
    public ScheduleResponseDto findById(Long id) {
        return new ScheduleResponseDto(scheduleRepository.findByIdOrElseThrow(id));
    }

    //스케줄 수정
    //수정할 스케줄은 id로 검색
    //유저는 jwt토큰에서 검색
    //스케줄 소유 유저가 jwt 소유 유저와 일치하면 수정 진행
    //스케줄의 제목, 본문은 각각 수정
    @Transactional
    public void updateSchedule(Long id, String title, String contents, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        User user = getUserByToken(request);

        if (!schedule.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}

        if (!title.isEmpty()){schedule.setTitle(title);}
        if (!contents.isEmpty()){schedule.setContents(contents);}
    }

    //스케줄 삭제
    //삭제할 스케줄은 id로 검색
    //유저는 jwt로 검색
    //스케줄 소유 유저가 jwt 소유 유저와 일치하면 삭제
    @Transactional
    public void deleteSchedule(Long id, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(id);
        User user = getUserByToken(request);
        if (!schedule.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");}
        scheduleRepository.delete(schedule);
    }

    //헤더에서 jwt토큰 추출-> jwt토큰의 sub에서 유저메일 추출-> 유저 메일로 유저 객체 검색해서 반환
    public User getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        return userRepository.findUserByMailOrElseThrow(sub);
    }
}
