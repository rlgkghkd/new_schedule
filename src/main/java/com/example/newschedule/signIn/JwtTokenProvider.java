package com.example.newschedule.signIn;

import com.example.newschedule.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    //jwt 토큰 제공자 생성자
    //$ openssl rand -hex 32로 생성한 키
    //받은 키를 BASE64로 디코드해서 키로 사용
    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey){
        byte[] keyByte= Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyByte);
    }

    //토큰 생성
    //전달받은 인증객체로 토큰 생성
    //엑세스 토큰은 토큰 subject와 기타 클레임, 수명을 지정해줌. 비밀키로 서명해서 압축
    //리프레시 토큰은 수명만 지정해서 서명함
    public JwtTokenDto makeToken(Authentication authen){
        String author= authen.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        String accToken = Jwts.builder().setSubject(authen.getName()).claim("auth", author).setExpiration(new Date(now + 1800000)).signWith(key, SignatureAlgorithm.HS256).compact();
        String refToken = Jwts.builder().setExpiration(new Date(now + 1800000)).signWith(key, SignatureAlgorithm.HS256).compact();
        return new JwtTokenDto("Bearer", accToken, refToken);
    }

    //토큰에 검증정보를 더함
    //토큰을 복호화해서 클레임을 받음
    //받은 클레임에 auth 정보가 비어있으면 예외처리
    //autho는 유저가 가진 roles 현재는 임의로 3개 생성해주었음.
    //pricipal은 유저 정보. 클레임에서 subject를 가져옴
    //반환되는 토큰은 pricipal, credentials, autho로 생성됨
    //credentials은 빈 문자열
    public Authentication getAuthen(String accToken){
        Claims claims = parseClaims(accToken);
        if (claims.get("auth") == null){ throw new RuntimeException("권한이 없는 토큰입니다.");}
        Collection<? extends GrantedAuthority> autho = Arrays.stream(claims.get("auth").toString().split(",")).map(SimpleGrantedAuthority::new).toList();
        UserDetails principal = new User(claims.getSubject(), "", autho);
        return new UsernamePasswordAuthenticationToken(principal, "", autho);
    }

    //토큰에서 subject 추출, subject는 토큰 생성한 유저의 메일로 설정되어있음.
    public String getTokenSubject(String token){
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    //토큰 검증
    //키를 이용해 복호화, 토큰의 타입 일치 여부, 만료여부, 지원여부, 클레임 을 검사함.
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e){ log.info("잘못된 JWT 토큰입니다", e);}
        catch (ExpiredJwtException e){log.info("토큰이 만료되었습니다.", e);}
        catch (UnsupportedJwtException e){log.info("지원하지 않는 토큰입니다.", e);}
        catch (IllegalArgumentException e){log.info("JWT 클레임이 비었습니다.", e);}
        return false;
    }

    //토큰 복호화
    private Claims parseClaims (String accToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accToken).getBody();
        } catch (ExpiredJwtException e) {return e.getClaims();}
    }
}
