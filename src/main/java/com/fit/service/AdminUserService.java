package com.fit.service;

import com.fit.common.PageResult;
import com.fit.dto.admin.AdminUserQueryDTO;
import com.fit.dto.user.UserDTO;
import com.fit.dto.user.UserProfileDTO;
public interface AdminUserService {
    PageResult<UserDTO> listUsers(AdminUserQueryDTO query);
    UserDTO getUserDetail(Long id);
    UserProfileDTO getUserProfile(Long userId);
    void updateUserStatus(Long id, String status);
    void updateUserRole(Long id, String role);
    void deleteUser(Long id);
}
