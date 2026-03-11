package com.fit.service.impl;

import com.fit.common.BusinessException;
import com.fit.domain.User;
import com.fit.domain.UserProfile;
import com.fit.dto.auth.LoginResponse;
import com.fit.dto.user.*;
import com.fit.mapper.UserMapper;
import com.fit.mapper.UserProfileMapper;
import com.fit.service.UserService;
import com.fit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse getMyInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserProfile profile = userProfileMapper.selectByUserId(userId);

        UserDTO userDTO = toUserDTO(user);
        UserProfileDTO profileDTO = profile != null ? toProfileDTO(profile) : null;

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, "Bearer", userDTO, profileDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (userMapper.countByEmail(dto.getEmail()) > 0) {
                throw new BusinessException("邮箱已被使用");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, UserProfileInputDTO dto) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setGender(dto.getGender());
            profile.setAge(dto.getAge());
            profile.setHeightCm(dto.getHeightCm());
            profile.setWeightKg(dto.getWeightKg());
            profile.setActivityLevel(dto.getActivityLevel());
            profile.setGoal(dto.getGoal());
            userProfileMapper.insert(profile);
        } else {
            profile.setGender(dto.getGender());
            profile.setAge(dto.getAge());
            profile.setHeightCm(dto.getHeightCm());
            profile.setWeightKg(dto.getWeightKg());
            profile.setActivityLevel(dto.getActivityLevel());
            profile.setGoal(dto.getGoal());
            userProfileMapper.update(profile);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, PasswordUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new BusinessException("原密码错误");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.update(user);
    }

    @Override
    public MetabolismDTO getMyMetabolism(Long userId) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            throw new BusinessException("请先完善身体数据");
        }

        return computeMetabolismFromProfile(profile);
    }

    @Override
    public MetabolismDTO computeMetabolism(UserProfileInputDTO dto) {
        UserProfile profile = new UserProfile();
        profile.setGender(dto.getGender());
        profile.setAge(dto.getAge());
        profile.setHeightCm(dto.getHeightCm());
        profile.setWeightKg(dto.getWeightKg());
        profile.setActivityLevel(dto.getActivityLevel());

        return computeMetabolismFromProfile(profile);
    }

    private MetabolismDTO computeMetabolismFromProfile(UserProfile profile) {
        Double bmr = calculateBMR(profile);
        Double activityFactor = getActivityFactor(profile.getActivityLevel());
        Double tdee = bmr * activityFactor;

        return new MetabolismDTO(bmr, activityFactor, tdee);
    }

    private Double calculateBMR(UserProfile profile) {
        Double weight = profile.getWeightKg();
        Double height = profile.getHeightCm();
        Integer age = profile.getAge();
        String gender = profile.getGender();

        if (weight == null || height == null || age == null || gender == null) {
            throw new BusinessException("身体数据不完整");
        }

        if ("male".equalsIgnoreCase(gender)) {
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else if ("female".equalsIgnoreCase(gender)) {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        } else {
            throw new BusinessException("性别参数错误，应为 male 或 female");
        }
    }

    private Double getActivityFactor(String activityLevel) {
        if (activityLevel == null) {
            return 1.2;
        }

        return switch (activityLevel.toLowerCase()) {
            case "sedentary", "no_exercise" -> 1.2;
            case "lightly_active", "light_exercise" -> 1.375;
            case "moderately_active", "moderate_exercise" -> 1.55;
            case "very_active", "hard_exercise" -> 1.725;
            case "extra_active", "athlete" -> 1.9;
            default -> 1.55;
        };
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

    private UserProfileDTO toProfileDTO(UserProfile p) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(p.getId());
        dto.setUserId(p.getUserId());
        dto.setGender(p.getGender());
        dto.setAge(p.getAge());
        dto.setHeightCm(p.getHeightCm());
        dto.setWeightKg(p.getWeightKg());
        dto.setActivityLevel(p.getActivityLevel());
        dto.setGoal(p.getGoal());
        return dto;
    }
}
