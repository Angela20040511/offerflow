package com.offerflow.mapper;

import com.offerflow.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByUsernameAndRole(@Param("username") String username, @Param("role") String role);
    User selectById(@Param("id") Long id);
    User selectByUsername(@Param("username") String username);
    int insertUser(User user);
}
