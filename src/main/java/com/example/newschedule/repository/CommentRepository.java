package com.example.newschedule.repository;

import com.example.newschedule.entity.Comment;
import com.example.newschedule.entity.Schedule;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    //스케줄에 종속된 모든 댓글 검색
    //PageImpl 타입으로 페이징 해서 반환함.
    Optional<List<Comment>> findAllBySchedule(Schedule schedule);
    default PageImpl<Comment> findAllByScheduleOrElseThorw(Schedule schedule, PageRequest pageRequest){
        List<Comment> commentList = findAllBySchedule(schedule).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"No comment was made on schedule " + schedule.getTitle()));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()), commentList.size());
        return new PageImpl<>(commentList.subList(start, end), pageRequest, commentList.size());
    }


    //Id 기반 댓글 검색
    default Comment findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "No comment has such id " + id));
    }

}
