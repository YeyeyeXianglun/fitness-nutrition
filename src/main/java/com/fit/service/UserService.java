package com.fit.service;

import com.fit.dto.auth.LoginResponse;
import com.fit.dto.user.*;

public interface UserService {
    LoginResponse getMyInfo(Long userId);
    void updateInfo(Long userId, UserUpdateDTO dto);
    void updateProfile(Long userId, UserProfileInputDTO dto);
    void updatePassword(Long userId, PasswordUpdateDTO dto);
    MetabolismDTO getMyMetabolism(Long userId);
    MetabolismDTO computeMetabolism(UserProfileInputDTO dto);
}
