package com.example.newschedule.controller;

import com.example.newschedule.dto.*;
import com.example.newschedule.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //로그인
    //dto 메일과 비밀번호로 유저 검증
    //jwt 토큰 반환
    @PostMapping("/signIn")
    public JwtTokenDto signIn(@RequestBody @Validated SignInRequestDto dto){
        String mail = dto.getMail();
        String password = dto.getPassword();

        return userService.signIn(mail, password);
    }

    //유저 생성
    //dto 이름, 메일, 패스워드
    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody @Validated SaveUserRequestDto dto){
        UserResponseDto userResponseDto = userService.saveUser(dto.getName(), dto.getMail(), dto.getPassword());
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    //아이디 기반 유저 검색
    //jwt 토큰 필요
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto userResponseDto= userService.findById(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.FOUND);
    }

    //유저 비밀번호 수정
    //jwt 토큰으로 수정할 유저 검색
    //dto 구 비밀번호, 신규 비밀번호
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Validated UpdateUserPasswordRequestDto dto, HttpServletRequest request){
        userService.updatePassword(request, dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //유저 메일 수정
    //jwt 토큰으로 수정할 유저 검색
    //dto 비밀번호, 신규 메일 주소
    @PatchMapping("/mail")
    public ResponseEntity<Void> updateMail(@RequestBody @Validated UpdateUserEmailRequestDto dto, HttpServletRequest request){
        userService.updateMail(request, dto.getPassword(), dto.getMail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //유저 삭제
    //jwt 토큰으로 삭제할 유저 검색
    //dto 비밀번호
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody @Validated DeleteUserRequestDto dto, HttpServletRequest request){
        userService.deleteUser(request, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
