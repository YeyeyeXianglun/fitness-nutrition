package com.fit.service.impl;

import com.fit.common.BusinessException;
import com.fit.domain.User;
import com.fit.domain.UserProfile;
import com.fit.dto.auth.LoginRequest;
import com.fit.dto.auth.LoginResponse;
import com.fit.dto.auth.RegisterRequest;
import com.fit.dto.user.UserDTO;
import com.fit.dto.user.UserProfileDTO;
import com.fit.mapper.UserMapper;
import com.fit.mapper.UserProfileMapper;
import com.fit.service.AuthService;
import com.fit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse register(RegisterRequest req) {
        if (userMapper.countByUsername(req.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }
        if (req.getEmail() != null && !req.getEmail().isBlank() && userMapper.countByEmail(req.getEmail()) > 0) {
            throw new BusinessException("邮箱已被注册");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        userMapper.insert(user);

        if (req.getProfile() != null) {
            UserProfile profile = new UserProfile();
            profile.setUserId(user.getId());
            profile.setGender(req.getProfile().getGender());
            profile.setAge(req.getProfile().getAge());
            profile.setHeightCm(req.getProfile().getHeightCm());
            profile.setWeightKg(req.getProfile().getWeightKg() != null ? req.getProfile().getWeightKg().doubleValue() : null);
            profile.setActivityLevel(req.getProfile().getActivityLevel());
            profile.setGoal(req.getProfile().getGoal());
            userProfileMapper.insert(profile);
        }

        return buildLoginResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        User user = userMapper.selectByUsernameWithPassword(req.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == null || !"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new BusinessException("账号已被禁用");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("用户名或密码错误");
        }
        return buildLoginResponse(user);
    }

    private LoginResponse buildLoginResponse(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        UserDTO userDTO = toUserDTO(user);
        UserProfileDTO profileDTO = null;
        UserProfile profile = userProfileMapper.selectByUserId(user.getId());
        if (profile != null) {
            profileDTO = toProfileDTO(profile);
        }
        return new LoginResponse(token, "Bearer", userDTO, profileDTO);
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
