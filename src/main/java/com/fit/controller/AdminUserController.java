package com.fit.controller;

import com.fit.common.PageResult;
import com.fit.common.Result;
import com.fit.dto.admin.AdminUserQueryDTO;
import com.fit.dto.user.UserDTO;
import com.fit.dto.user.UserProfileDTO;
import com.fit.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "管理员-用户管理", description = "用户列表、详情、状态/角色修改、删除（需管理员权限）")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "分页查询用户", description = "支持 keyword、role、status；使用 PageHelper 分页")
    public Result<PageResult<UserDTO>> list(AdminUserQueryDTO query) {
        PageResult<UserDTO> page = adminUserService.listUsers(query);
        return Result.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public Result<UserDTO> getDetail(@PathVariable Long id) {
        UserDTO user = adminUserService.getUserDetail(id);
        return Result.ok(user);
    }

    @GetMapping("/{id}/profile")
    @Operation(summary = "获取用户档案")
    public Result<UserProfileDTO> getProfile(@PathVariable Long id) {
        UserProfileDTO profile = adminUserService.getUserProfile(id);
        return Result.ok(profile);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "修改用户状态", description = "如 ACTIVE / INACTIVE")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        adminUserService.updateUserStatus(id, status);
        return Result.ok();
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "修改用户角色", description = "如 USER / ADMIN")
    public Result<Void> updateRole(@PathVariable Long id, @RequestParam String role) {
        adminUserService.updateUserRole(id, role);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "同时删除其档案")
    public Result<Void> delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return Result.ok();
    }
}
