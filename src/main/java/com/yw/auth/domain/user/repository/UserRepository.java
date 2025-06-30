package com.yw.auth.domain.user.repository;

import com.yw.auth.domain.user.repository.vo.UserLoginVO;

import java.util.Optional;

public interface UserRepository {

    Optional<UserLoginVO> getUserByLoginIdAndPassword(String userId, String userPassword);
}
