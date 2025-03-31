package com.example.newschedule.controller;

import com.example.newschedule.dto.*;
import com.example.newschedule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody SaveUserRequestDto dto){
        UserResponseDto userResponseDto = userService.saveUser(dto.getName(), dto.getMail(), dto.getPassword());
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto userResponseDto= userService.findById(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.FOUND);
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UpdateUserPasswordRequestDto dto){
        userService.updatePassword(id, dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/mail/{id}")
    public ResponseEntity<Void> updateMail(@PathVariable Long id, @RequestBody UpdateUserEmailRequestDto dto){
        userService.updateMail(id, dto.getPassword(), dto.getMail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestBody DeleteUserRequestDto dto){
        userService.deleteUser(id, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
