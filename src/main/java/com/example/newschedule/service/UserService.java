package com.example.newschedule.service;

import com.example.newschedule.dto.UserResponseDto;
import com.example.newschedule.entity.User;
import com.example.newschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto saveUser(String userName, String mail, String password) {
        User user= new User(userName, mail, password);
        return new UserResponseDto(userRepository.save(user));
    }

    public UserResponseDto findById(Long id) {
        return new UserResponseDto(userRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findByIdOrElseThrow(id);
        if (!user.getPassword().equals(oldPassword)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setPassword(newPassword);
    }

    @Transactional
    public void updateMail(Long id, String password, String mail) {
        User user = userRepository.findByIdOrElseThrow(id);
        if (!user.getPassword().equals(password)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setMail(mail);
    }

    @Transactional
    public void deleteUser(Long id, String password) {
        User user = userRepository.findByIdOrElseThrow(id);
        if (!user.getPassword().equals(password)){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        userRepository.delete(user);
    }

}
