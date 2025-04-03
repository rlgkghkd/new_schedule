package com.example.newschedule.repository;

import com.example.newschedule.entity.Schedule;
import com.example.newschedule.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "No schedule has such id " + id));
    }

    Optional<List<Schedule>> findAllByUser(User user);
    default PageImpl<Schedule> findAllByUserOrElseThrow(User user, PageRequest pageRequest){
        List<Schedule> scheduleList = findAllByUser(user).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No schedule own by such user "+ user.getName()));
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start+pageRequest.getPageSize()), scheduleList.size());
        return new PageImpl<>(scheduleList.subList(start, end), pageRequest, scheduleList.size());
    }

}
