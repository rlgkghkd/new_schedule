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

    
    //유저 로그인
    //유저 인증용 토큰 생성
    //인증용 토큰으로 인증 객체 생성
    //인증 객체로 jwt토큰 생성
    @Transactional
    public JwtTokenDto signIn(String mail, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(mail, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.makeToken(authentication);
    }

    //유저 생성
    //이름, 메일주소, 비밀번호
    // 비밀번호는 암호화해서 저장.
    //유저 생성시 역활은 USER, asd, qweqwe 3개를 만들어줌.
    public UserResponseDto saveUser(String userName, String mail, String password) {
        User user= new User(userName, mail, passwordEncoder.encode(password), "USER", "asd", "qweqwe");
        return new UserResponseDto(userRepository.save(user));
    }

    //유저 id 기반 검색
    public UserResponseDto findById(Long id) {
        return new UserResponseDto(userRepository.findByIdOrElseThrow(id));
    }

    //유저 패스워드 수정
    //유저 정보는 jwt 토큰으로 검색
    //패스워드 인코더의 matches 메서드로 받은 패스워드와 기존 패스워드 일치 검사
    //일치시 신 패스워드를 암호화해서 저장
    @Transactional
    public void updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {
        User user = getUserByToken(request);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    //유저 메일 수정
    //유저 정보는 jwt 토큰으로 검색
    //패스워드 인코더의 matches 메서드로 받은 패스워드와 기존 패스워드 일치 검사
    //일치시 메일 수정
    @Transactional
    public void updateMail(HttpServletRequest request, String password, String mail) {
        User user = getUserByToken(request);

        if (!passwordEncoder.matches(password, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        user.setMail(mail);
    }

    //유저 삭제
    //유저 정보는 jwt 토큰으로 검색
    //패스워드 인코더의 matches 메서드로 받은 패스워드와 기존 패스워드 일치 검사
    //일치시 유저 삭제
    @Transactional
    public void deleteUser(HttpServletRequest request, String password) {
        User user = getUserByToken(request);

        if (!passwordEncoder.matches(password, user.getPassword())){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "패스워드가 일치하지 않습니다.");}
        userRepository.delete(user);
    }

    //jwt 토큰에서 유저 탐색 메서드
    public User getUserByToken(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String sub = jwtTokenProvider.getTokenSubject(token);
        return userRepository.findUserByMailOrElseThrow(sub);
    }
}
