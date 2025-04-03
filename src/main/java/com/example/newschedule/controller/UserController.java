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

    @PostMapping("/signIn")
    public JwtTokenDto signIn(@RequestBody @Validated SignInRequestDto dto){
        String mail = dto.getMail();
        String password = dto.getPassword();

        return userService.signIn(mail, password);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody @Validated SaveUserRequestDto dto){
        UserResponseDto userResponseDto = userService.saveUser(dto.getName(), dto.getMail(), dto.getPassword());
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto userResponseDto= userService.findById(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.FOUND);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Validated UpdateUserPasswordRequestDto dto, HttpServletRequest request){
        userService.updatePassword(request, dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/mail")
    public ResponseEntity<Void> updateMail(@RequestBody @Validated UpdateUserEmailRequestDto dto, HttpServletRequest request){
        userService.updateMail(request, dto.getPassword(), dto.getMail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody @Validated DeleteUserRequestDto dto, HttpServletRequest request){
        userService.deleteUser(request, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
