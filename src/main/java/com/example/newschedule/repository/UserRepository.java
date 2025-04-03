package com.example.newschedule.repository;

import com.example.newschedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    default User findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User has such id " + id));
    }

    Optional<User> findUserByMail (String mail);
    default User findUserByMailOrElseThrow(String mail){
        return findUserByMail(mail).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No User has such mail " + mail));
    }
}
