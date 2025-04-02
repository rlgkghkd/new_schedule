package com.example.newschedule.service;

import com.example.newschedule.entity.User;
import com.example.newschedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException{
        return userRepository.findUserByMail(userMail).map(this::creteUserDetails).orElseThrow(()-> new UsernameNotFoundException("없음"));
    }

    private UserDetails creteUserDetails(User user){
        return User.builder()
                .name(user.getName())
                .password(user.getPassword())
                .mail(user.getMail())
                .roles(Arrays.stream(user.getRoles().toArray(new String[0])).toList())
                .build();
    }
}
