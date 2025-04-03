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

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey){
        byte[] keyByte= Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyByte);
    }

    public JwtTokenDto makeToken(Authentication authen){
        String author= authen.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        String accToken = Jwts.builder().setSubject(authen.getName()).claim("auth", author).setExpiration(new Date(now + 1800000)).signWith(key, SignatureAlgorithm.HS256).compact();
        String refToken = Jwts.builder().setExpiration(new Date(now + 1800000)).signWith(key, SignatureAlgorithm.HS256).compact();
        return new JwtTokenDto("Bearer", accToken, refToken);
    }

    public Authentication getAuthen(String accToken){
        Claims claims = parseClaims(accToken);
        if (claims.get("auth") == null){ throw new RuntimeException("권한이 없는 토큰입니다.");}
        Collection<? extends GrantedAuthority> autho = Arrays.stream(claims.get("auth").toString().split(",")).map(SimpleGrantedAuthority::new).toList();
        UserDetails principal = new User(claims.getSubject(), "", autho);
        return new UsernamePasswordAuthenticationToken(principal, "", autho);
    }

    public String getTokenSubject(String token){
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

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

    private Claims parseClaims (String accToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accToken).getBody();
        } catch (ExpiredJwtException e) {return e.getClaims();}
    }
}
