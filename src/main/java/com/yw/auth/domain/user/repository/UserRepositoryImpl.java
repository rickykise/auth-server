package com.yw.auth.domain.user.repository;

import com.yw.auth.domain.user.repository.mapper.UserMapper;
import com.yw.auth.domain.user.repository.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<UserLoginVO> getUserByLoginIdAndPassword(String userId, String userPassword) {
        return Optional.ofNullable(userMapper.getUserByLoginIdAndPassword(userId, userPassword));
    }
}
