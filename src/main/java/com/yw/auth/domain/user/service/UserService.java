package com.yw.auth.domain.user.service;

import com.yw.auth.domain.user.repository.UserRepository;
import com.yw.auth.domain.user.repository.vo.UserLoginVO;
import com.yw.auth.global.config.JwtTokenProvider;
import com.yw.auth.global.exception.BusinessException;
import com.yw.auth.global.response.CommonResponse;
import com.yw.auth.global.service.RefreshTokenService;
import com.yw.auth.global.util.SHAUtil;
import com.yw.auth.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    private static final long ACCESS_TOKEN_EXPIRY = 15 * 60 * 1000; // 15분
    private static final long REFRESH_TOKEN_EXPIRY = 7 * 24 * 60 * 60 * 1000L; // 7일

    public ResponseEntity<CommonResponse<Object>> loginFromAccessToken(String loginId, String userPassword)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UserLoginVO user = userRepository.getUserByLoginIdAndPassword(
                        loginId, SHAUtil.encrypt(userPassword))
                .orElseThrow(() -> new BusinessException("아이디 및 패스워드를 다시 확인해 주십시오."));

        return createLoginResponse(
                user.getUserId(), user.getRole(), user.getUserNm(), user.getLoginId()
        );
    }

    public ResponseEntity<CommonResponse<Object>> reissueToken(String requestRefreshToken) {
        if (jwtTokenProvider.isTokenExpired(requestRefreshToken)) {
            throw new BusinessException("RefreshToken이 만료되었습니다.");
        }

        String loginId = jwtTokenProvider.getLoginIdFromToken(requestRefreshToken);
        String savedRefreshToken = refreshTokenService.getRefreshToken(loginId);

        if (!savedRefreshToken.equals(requestRefreshToken)) {
            throw new BusinessException("RefreshToken이 일치하지 않습니다.");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(savedRefreshToken);
        String userRole = jwtTokenProvider.getRoleFromToken(savedRefreshToken);
        String userNm = jwtTokenProvider.getUserNmFromToken(savedRefreshToken);

        return createLoginResponse(userId, userRole, userNm, loginId);
    }

    private ResponseEntity<CommonResponse<Object>> createLoginResponse(String userId, String role, String userNm, String loginId) {
        String accessToken = jwtTokenProvider.createToken(userId, role, userNm, loginId, ACCESS_TOKEN_EXPIRY);
        String refreshToken = jwtTokenProvider.createToken(userId, role, userNm, loginId, REFRESH_TOKEN_EXPIRY);

        refreshTokenService.deleteRefreshToken(loginId); // idempotent하게 처리
        refreshTokenService.saveRefreshToken(loginId, refreshToken);

        ResponseCookie refreshCookie = buildRefreshCookie(refreshToken);

        UserResponse.UserLoginResponse responseWithoutRefresh = new UserResponse.UserLoginResponse(
                loginId,
                userNm,
                accessToken
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(CommonResponse.success(responseWithoutRefresh));
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
    }
}
