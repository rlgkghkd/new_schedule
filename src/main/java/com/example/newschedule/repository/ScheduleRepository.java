package com.example.newschedule.repository;

import com.example.newschedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "No schedule has such id " + id));
    }
}
