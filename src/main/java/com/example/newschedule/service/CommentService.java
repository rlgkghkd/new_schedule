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

    public CommentResponseDto saveComment(Long scheduleId, String contents, HttpServletRequest request) {
        Schedule schedule= scheduleRepository.findByIdOrElseThrow(scheduleId);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByNameOrElseThrow(sub);

        Comment comment = new Comment(contents);
        comment.setSchedule(schedule);
        comment.setUser(user);
        schedule.addCommnts(comment);
        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(saved);
    }

    public List<CommentResponseDto> findAllBySchedule(Long scheduleId, int index) {
        PageRequest pageRequest = PageRequest.of(index-1, 10);
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return commentRepository.findAllByScheduleOrElseThorw(schedule, pageRequest).stream().map(CommentResponseDto::new).toList();
    }

    public CommentResponseDto findById(Long id) {
        return new CommentResponseDto(commentRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    public void updateComment(Long id, String contents, HttpServletRequest request) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByNameOrElseThrow(sub);
        if(!comment.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 댓글에 대한 권한이 없습니다.");}

        if (!contents.isEmpty()){comment.setContents(contents);}
    }

    @Transactional
    public void deleteComment(Long id, HttpServletRequest request) {
        Comment comment = commentRepository.findByIdOrElseThrow(id);

        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        User user = userRepository.findUserByNameOrElseThrow(sub);
        if(!comment.getUser().equals(user)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 댓글에 대한 권한이 없습니다.");}

        commentRepository.delete(comment);
    }
}
