package com.fit.controller;

import com.fit.common.Result;
import com.fit.dto.auth.LoginResponse;
import com.fit.dto.user.*;
import com.fit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "个人中心", description = "查看与修改个人信息、档案、密码（需登录）")
public class UserController {

    private final UserService userService;

    private Long currentUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @GetMapping("/profile")
    @Operation(summary = "获取我的个人信息", description = "含基本信息和档案（身高、性别、年龄、目标等）")
    public Result<LoginResponse> getMyProfile(HttpServletRequest request) {
        Long userId = currentUserId(request);
        LoginResponse data = userService.getMyInfo(userId);
        return Result.ok(data);
    }

    @PutMapping("/info")
    @Operation(summary = "修改基本信息", description = "修改邮箱、手机号")
    public Result<Void> updateInfo(HttpServletRequest request, @Valid @RequestBody UserUpdateDTO dto) {
        userService.updateInfo(currentUserId(request), dto);
        return Result.ok();
    }

    @PutMapping("/profile")
    @Operation(summary = "修改身体数据", description = "修改身高、性别、年龄、体重、活动等级、目标等")
    public Result<Void> updateProfile(HttpServletRequest request, @Valid @RequestBody UserProfileInputDTO dto) {
        userService.updateProfile(currentUserId(request), dto);
        return Result.ok();
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> updatePassword(HttpServletRequest request, @Valid @RequestBody PasswordUpdateDTO dto) {
        userService.updatePassword(currentUserId(request), dto);
        return Result.ok();
    }

    @GetMapping("/metabolism")
    @Operation(summary = "获取我的 BMR/TDEE", description = "基于已保存的身高/体重/年龄/性别和活动等级计算")
    public Result<MetabolismDTO> getMyMetabolism(HttpServletRequest request) {
        MetabolismDTO data = userService.getMyMetabolism(currentUserId(request));
        return Result.ok(data);
    }

    @PostMapping("/metabolism/compute")
    @Operation(summary = "计算 BMR/TDEE（不落库）", description = "基于输入的身高/体重/年龄/性别和活动等级计算")
    public Result<MetabolismDTO> compute(@Valid @RequestBody UserProfileInputDTO dto) {
        MetabolismDTO data = userService.computeMetabolism(dto);
        return Result.ok(data);
    }
}
