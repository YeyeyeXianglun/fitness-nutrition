package com.fit.mapper;

import com.fit.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProfileMapper {

    @Select("SELECT * FROM user_profile WHERE user_id = #{userId}")
    UserProfile selectByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM user_profile WHERE id = #{id}")
    UserProfile selectById(@Param("id") Long id);

    int insert(UserProfile profile);

    int update(UserProfile profile);

    int deleteByUserId(@Param("userId") Long userId);
}
