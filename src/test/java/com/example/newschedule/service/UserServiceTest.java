package com.example.newschedule.service;

import com.example.newschedule.entity.User;
import com.example.newschedule.repository.UserRepository;
import com.example.newschedule.signIn.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Nested()
class UserServiceTest {

    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        testUser = new User("testName", "testMail", "testPassword");
    }


    @ParameterizedTest
    @DisplayName("유저 생성 성공 여부 확인")
    @CsvSource({"asd, asd, asd", "qwe, qwe, qwe"})
    void saveUser(String name, String mail, String password) {
        User user = new User(name, mail, passwordEncoder.encode(password), "USER", "asd", "qweqwe");
        assertEquals(name, user.getName());
        assertEquals(mail, user.getMail());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }

    @Nested
    @DisplayName("유저 데이터 조작 테스트")
    class userModifie {

        @ParameterizedTest
        @DisplayName("유저 비밀번호 변경")
        @ValueSource(strings = {"first", "second", "third"})
        void updatePassword(String newPassword) {
            testUser.setPassword(passwordEncoder.encode(newPassword));
            assertTrue(passwordEncoder.matches(newPassword, testUser.getPassword()));
        }

        @ParameterizedTest
        @DisplayName("유저 메일 변경")
        @ValueSource(strings = {"first", "second", "third"})
        void updateMail(String newMail) {
            testUser.setMail(newMail);
            assertEquals(newMail, testUser.getMail());
        }
    }
}