package com.yw.auth.global.filter;

import com.yw.auth.global.config.JwtTokenProvider;
import com.yw.auth.global.exception.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final Set<String> EXCLUDE_URLS = Set.of(
            "/api/v1/user/auth/reissue",
            "/api/v1/user/auth/login"
    );

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 인증 제외 경로는 토큰 검사 없이 통과
        if (EXCLUDE_URLS.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null) {
                // 토큰에서 userId를 가져옴
                String userId = jwtTokenProvider.getUserIdFromToken(token);
                // request에 userId를 추가
                request.setAttribute("userId", userId);
            }

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            // 에러 처리
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
}
