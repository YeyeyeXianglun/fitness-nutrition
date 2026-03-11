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
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        UserDTO userDTO = toUserDTO(user);
        UserProfileDTO profileDTO = profile != null ? toProfileDTO(profile) : null;
        return new LoginResponse(token, "Bearer", userDTO, profileDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        userMapper.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, UserProfileInputDTO dto) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            if (dto.getGender() != null) profile.setGender(dto.getGender());
            if (dto.getAge() != null) profile.setAge(dto.getAge());
            if (dto.getHeightCm() != null) profile.setHeightCm(dto.getHeightCm());
            if (dto.getWeightKg() != null) profile.setWeightKg(dto.getWeightKg().doubleValue());
            if (dto.getActivityLevel() != null) profile.setActivityLevel(dto.getActivityLevel());
            if (dto.getGoal() != null) profile.setGoal(dto.getGoal());
            userProfileMapper.insert(profile);
        } else {
            if (dto.getGender() != null) profile.setGender(dto.getGender());
            if (dto.getAge() != null) profile.setAge(dto.getAge());
            if (dto.getHeightCm() != null) profile.setHeightCm(dto.getHeightCm());
            if (dto.getWeightKg() != null) profile.setWeightKg(dto.getWeightKg().doubleValue());
            if (dto.getActivityLevel() != null) profile.setActivityLevel(dto.getActivityLevel());
            if (dto.getGoal() != null) profile.setGoal(dto.getGoal());
            userProfileMapper.update(profile);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, PasswordUpdateDTO dto) {
        User u = userMapper.selectById(userId);
        if (u == null) {
            throw new BusinessException("用户不存在");
        }
        User user = userMapper.selectByUsernameWithPassword(u.getUsername());
        if (user == null || !passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new BusinessException("原密码错误");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(dto.getNewPassword()));
    }

    @Override
    public MetabolismDTO getMyMetabolism(Long userId) {
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        if (profile == null) {
            throw new BusinessException("未填写身体数据");
        }
        UserProfileInputDTO input = new UserProfileInputDTO();
        input.setGender(profile.getGender());
        input.setAge(profile.getAge());
        input.setHeightCm(profile.getHeightCm());
        input.setWeightKg(profile.getWeightKg());
        input.setActivityLevel(profile.getActivityLevel());
        input.setGoal(profile.getGoal());
        return computeMetabolism(input);
    }

    @Override
    public MetabolismDTO computeMetabolism(UserProfileInputDTO input) {
        if (input == null) {
            throw new BusinessException("参数不能为空");
        }
        if (input.getGender() == null || input.getAge() == null || input.getHeightCm() == null || input.getWeightKg() == null) {
            throw new BusinessException("计算 BMR/TDEE 需要性别、年龄、身高、体重");
        }

        double weight = input.getWeightKg();
        double height = input.getHeightCm();
        int age = input.getAge();

        double bmr;
        String g = input.getGender().trim().toUpperCase();
        if ("FEMALE".equals(g)) {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        } else {
            // MALE/OTHER 默认按男性公式（可在前端限制只有 MALE/FEMALE）
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        }

        double factor = activityFactor(input.getActivityLevel());
        double tdee = bmr * factor;
        return new MetabolismDTO(round1(bmr), round3(factor), round1(tdee));
    }

    private static double activityFactor(String activityLevel) {
        if (activityLevel == null || activityLevel.isBlank()) {
            // 默认：一周不有氧/久坐少动
            return 1.2;
        }
        String v = activityLevel.trim().toLowerCase();

        // 兼容你描述的三档：不有氧/一周3次/一周5次
        if (v.equals("none") || v.equals("no_cardio") || v.equals("cardio_0") || v.equals("sedentary")) {
            return 1.2;
        }
        if (v.equals("mid") || v.equals("cardio_3") || v.equals("three_per_week") || v.equals("moderately_active")) {
            return 1.55;
        }
        if (v.equals("high") || v.equals("cardio_5") || v.equals("five_per_week") || v.equals("very_active")) {
            return 1.725;
        }

        // 兼容历史值（保留原 DTO 的可选项）
        if (v.equals("lightly_active")) return 1.375;
        if (v.equals("extra_active")) return 1.9;

        return 1.2;
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }

    private static double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }

    private static UserDTO toUserDTO(User user) {
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

    private static UserProfileDTO toProfileDTO(UserProfile p) {
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
