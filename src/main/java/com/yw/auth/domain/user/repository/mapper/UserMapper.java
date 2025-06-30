package com.yw.auth.domain.user.repository.mapper;

import com.yw.auth.domain.user.repository.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    UserLoginVO getUserByLoginIdAndPassword(@Param("userId") String userId, @Param("userPassword") String userPassword);
}
