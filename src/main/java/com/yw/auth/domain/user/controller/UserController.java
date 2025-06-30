package com.yw.auth.domain.user.controller;

import com.yw.auth.domain.user.service.UserService;
import com.yw.auth.global.config.SwaggerConfig;
import com.yw.auth.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Tag(name = SwaggerConfig.SwaggerTags.USER)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "로그인",
            description = "사용자 인증 및 JWT 발급\n\n" +
                    "### 인증 절차\n" +
                    "1. 로그인\n" +
                    "2. Response의 accessToken 복사\n" +
                    "3. Authorize에 accessToken 입력")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<Object>> loginFromAccessToken(
            @RequestParam(required = false) @Parameter(description = "아이디") String loginId,
            @RequestParam(required = false) @Parameter(description = "비밀번호", schema = @Schema(format = "password")) String userPassword
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.loginFromAccessToken(loginId, userPassword);
    }

    @Operation(summary = "JWT 토큰 재발급",
            description = "사용자 인증 및 JWT 발급\n\n" +
                    "### 토큰 재발급 절차\n" +
                    "1. Access Token이 만료되었을 경우, 저장된 Refresh Token을 사용해 새로운 Access Token을 발급합니다.\n" +
                    "2. 이 엔드포인트는 로그인된 사용자의 Refresh Token을 기반으로 작동합니다.\n" +
                    "3. Refresh Token은 보통 HttpOnly 쿠키 혹은 Authorization 헤더로 전달됩니다.")
    @PostMapping("/auth/reissue")
    public ResponseEntity<CommonResponse<Object>> reissueToken(@RequestHeader("refresh-token") String refreshToken) {
        return userService.reissueToken(refreshToken);
    }
}
