package com.example.newschedule.signIn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//spring-boot-starter-security의 설정
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private  final  JwtTokenProvider jwtTokenProvider;

    //rest컨트롤러이므로 httpbasic, csrf는 비활성화함
    //세션 관리, http요청 인증을 필터링함
    // 유저 로그인 도메인과 유저 생성 도메인은 모두 허용, 이외의 도메인은 인증 정보가 있어야 접근 가능
    //인증 정보는 jwt 토큰으로 제공함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a-> a
                        .requestMatchers("/users/signIn").permitAll()
                        .requestMatchers("/users").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    //패스워드 인코더
    //BCryptPasswordEncoder 사용
    //인코딩 기능, 인코딩 된 비밀번호와 플레인 텍스트 비밀번호의 비교기능 제공.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
