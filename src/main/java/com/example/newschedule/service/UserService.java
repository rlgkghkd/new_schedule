package com.example.newschedule.service;

import com.example.newschedule.signIn.JwtTokenProvider;
import com.example.newschedule.dto.JwtTokenDto;
import com.example.newschedule.dto.UserResponseDto;
import com.example.newschedule.entity.User;
import com.example.newschedule.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtTokenDto signIn(String mail, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mail, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.makeToken(authentication);
    }

    public UserResponseDto saveUser(String userName, String mail, String password) {
        User user= new User(userName, mail, passwordEncoder.encode(password), "USER", "asd", "qweqwe");
        return new UserResponseDto(userRepository.save(user));
    }

    public UserResponseDto findById(Long id) {
        return new UserResponseDto(userRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    public void updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {
        User user = getUserByToken(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(oldPassword, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setPassword(encoder.encode(newPassword));
    }

    @Transactional
    public void updateMail(HttpServletRequest request, String password, String mail) {
        User user = getUserByToken(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setMail(mail);
    }

    @Transactional
    public void deleteUser(HttpServletRequest request, String password) {
        User user = getUserByToken(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        userRepository.delete(user);
    }

    public User getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        return userRepository.findUserByMailOrElseThrow(sub);
    }
}
