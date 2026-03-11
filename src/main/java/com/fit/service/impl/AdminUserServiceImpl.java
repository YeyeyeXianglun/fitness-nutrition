package com.fit.service.impl;

import com.fit.common.BusinessException;
import com.fit.common.PageResult;
import com.fit.domain.User;
import com.fit.domain.UserProfile;
import com.fit.dto.admin.AdminUserQueryDTO;
import com.fit.dto.user.UserDTO;
import com.fit.dto.user.UserProfileDTO;
import com.fit.mapper.UserMapper;
import com.fit.mapper.UserProfileMapper;
import com.fit.service.AdminUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public PageResult<UserDTO> listUsers(AdminUserQueryDTO query) {
        PageHelper.startPage(query.getPageNum() != null ? query.getPageNum() : 1,
                query.getPageSize() != null ? query.getPageSize() : 10);
        List<User> list = userMapper.selectList(query.getKeyword(), query.getRole(), query.getStatus());
        PageInfo<User> pageInfo = new PageInfo<>(list);
        List<UserDTO> dtoList = pageInfo.getList().stream().map(this::toUserDTO).collect(Collectors.toList());
        PageInfo<UserDTO> dtoPage = new PageInfo<>();
        dtoPage.setList(dtoList);
        dtoPage.setTotal(pageInfo.getTotal());
        dtoPage.setPageNum(pageInfo.getPageNum());
        dtoPage.setPageSize(pageInfo.getPageSize());
        dtoPage.setPages(pageInfo.getPages());
        return PageResult.of(dtoPage);
    }

    @Override
    public UserDTO getUserDetail(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toUserDTO(user);
    }

    @Override
    public UserProfileDTO getUserProfile(Long userId) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            return null;
        }
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setGender(profile.getGender());
        dto.setAge(profile.getAge());
        dto.setHeightCm(profile.getHeightCm());
        dto.setWeightKg(profile.getWeightKg());
        dto.setActivityLevel(profile.getActivityLevel());
        dto.setGoal(profile.getGoal());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long id, String status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(Long id, String role) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRole(role);
        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        if (userMapper.selectById(id) == null) {
            throw new BusinessException("用户不存在");
        }
        userProfileMapper.deleteByUserId(id);
        userMapper.deleteById(id);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
