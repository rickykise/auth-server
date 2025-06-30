package com.yw.auth.domain.user.repository.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginVO {
    String loginId;
    String userId;
    String userNm;
    String role;
}
