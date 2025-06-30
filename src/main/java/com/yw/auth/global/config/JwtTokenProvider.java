package com.yw.auth.global.config;

import com.yw.auth.domain.user.repository.UserRepository;
import com.yw.auth.global.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, @Value("${spring.jwt.valid-duration}") Duration validDuration, UserRepository userRepository) {
        // 키 생성
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userId, String role, String userNm, String loginId, long validity) {
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("role", role);
        claims.put("userNm", userNm);
        claims.put("userId", userId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰에서 값 추출하기
    public String resolveToken(HttpServletRequest request) {  // HttpServletRequest 사용
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization header: {}", bearerToken);  // 디버깅용

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // JWT에서 로그인 아이디 가져오기
    public String getLoginIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // JWT에서 역할 정보 가져오기
    public String getUserIdFromToken(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().get("userId");
    }

    // JWT에서 역할 정보 가져오기
    public String getUserNmFromToken(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().get("userNm");
    }

    // JWT에서 역할 정보 가져오기
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().get("role");
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException("유효하지 않은 토큰입니다.");
        }
    }
}
