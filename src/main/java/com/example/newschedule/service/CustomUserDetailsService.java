package com.example.newschedule.service;

import com.example.newschedule.entity.User;
import com.example.newschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;


//authenticationManagerBuilder가 사용하는 유저 디테일 서비스
//jwt토큰을 생성할 때 유저의 정보를 인증객체 형태로 보내줌
//유저의 이름 대신 유저의 메일 사용
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException{
        return userRepository.findUserByMail(userMail).map(this::creteUserDetails).orElseThrow(()-> new UsernameNotFoundException("없음"));
    }

    private UserDetails creteUserDetails(User user){
        return User.builder()
                .name(user.getMail())
                .password(user.getPassword())
                .mail(user.getName())
                .roles(Arrays.stream(user.getRoles().toArray(new String[0])).toList())
                .build();
    }
}
