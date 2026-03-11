package com.fit.controller;

import com.fit.common.Result;
import com.fit.dto.auth.LoginRequest;
import com.fit.dto.auth.LoginResponse;
import com.fit.dto.auth.RegisterRequest;
import com.fit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块", description = "登录、注册（无需令牌）")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证账号密码，返回 JWT 及用户信息")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "填写账号、密码及个人数据，注册成功后返回令牌与信息")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return Result.ok(response);
    }
}
