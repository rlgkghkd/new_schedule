package com.example.newschedule.service;

import com.example.newschedule.dto.CommentResponseDto;
import com.example.newschedule.dto.ScheduleResponseDto;
import com.example.newschedule.entity.BaseEntity;
import com.example.newschedule.entity.Comment;
import com.example.newschedule.entity.Schedule;
import com.example.newschedule.entity.User;
import com.example.newschedule.repository.CommentRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    //댓글 생성
    //scheduleRepository에서 스케줄 가져옴
    //유저 정보는 HttpServletRequest으로 받은 jwt 토큰 복호화
    //jwt 토큰의 sub은 소유주의 메일주소로 되어있음
    //comment 객체를 생성하고 종속할 스케줄, 유저를 세팅한 뒤 저장
    public CommentResponseDto saveComment(Long scheduleId, String contents, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(scheduleId);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByMailOrElseThrow(sub);

        Comment comment = new Comment(contents);
        comment.setSchedule(schedule);
        comment.setUser(user);
        schedule.addCommnts(comment);
        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(saved);
    }

    //댓글 전부 검색
    //인덱스는 받은 숫자에서 -1
    //리포지토리에 PageRequest와 함께 넘겨줌
    //반환 받은 객체는 PageImpl<Comment> 타입, dto로 매핑하고 리스트로 전환해 반환함
    public List<CommentResponseDto> findAllBySchedule(Long scheduleId, int index) {
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return commentRepository.findAllByScheduleOrElseThorw(schedule, pageRequest).stream().map(CommentResponseDto::new).toList();
    }

    //댓글 id로 검색
    public CommentResponseDto findById(Long id) {
        return new CommentResponseDto(commentRepository.findByIdOrElseThrow(id));
    }

    //댓글 수정
    //수정할 댓글은 id로 검색
    //수정할 댓글의 소유 유저를 jwt토큰 소유 유저와 비교, 일치하면 수정
    //복호화 한 jwt 토큰의 sub이 로그인 유저의 메일
    @Transactional
    public void updateComment(Long id, String contents, HttpServletRequest request) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByMailOrElseThrow(sub);
        if(!comment.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 댓글에 대한 권한이 없습니다.");}

        if (!contents.isEmpty()){comment.setContents(contents);}
    }

    //댓글 삭제
    //삭제할 댓글은 id로 검색
    //삭제할 댓글의 소유 유저를 jwt토큰 소유 유저와 비교, 일치하면 삭제
    @Transactional
    public void deleteComment(Long id, HttpServletRequest request) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByMailOrElseThrow(sub);
        if(!comment.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 댓글에 대한 권한이 없습니다.");}

        commentRepository.delete(comment);
    }
}
