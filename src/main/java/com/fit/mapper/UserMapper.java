package com.fit.mapper;

import com.fit.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(@Param("id") Long id);
    
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsernameWithPassword(@Param("username") String username);
    
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
    
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(@Param("email") String email);
    
    List<User> selectList(@Param("keyword") String keyword, 
                         @Param("role") String role, 
                         @Param("status") String status);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(@Param("id") Long id);
}
