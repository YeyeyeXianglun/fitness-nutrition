package com.fit.service;

import com.fit.dto.auth.LoginResponse;
import com.fit.dto.user.PasswordUpdateDTO;
import com.fit.dto.user.UserProfileInputDTO;
import com.fit.dto.user.UserUpdateDTO;
import com.fit.dto.user.MetabolismDTO;

public interface UserService {
    LoginResponse getMyInfo(Long userId);
    void updateInfo(Long userId, UserUpdateDTO dto);
    void updateProfile(Long userId, UserProfileInputDTO dto);
    void updatePassword(Long userId, PasswordUpdateDTO dto);

    /**
     * 基于当前用户档案计算 BMR/TDEE
     */
    MetabolismDTO getMyMetabolism(Long userId);

    /**
     * 基于输入参数计算 BMR/TDEE（不落库）
     */
    MetabolismDTO computeMetabolism(UserProfileInputDTO input);
}
