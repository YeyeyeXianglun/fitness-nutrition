package com.fit.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "注册请求")
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "zhangsan", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456", required = true)
    private String password;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Valid
    @Schema(description = "个人身体数据")
    private ProfileData profile;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "个人身体数据")
    public static class ProfileData {

        @NotNull(message = "性别不能为空")
        @Schema(description = "性别", example = "male", required = true)
        private String gender;

        @NotNull(message = "年龄不能为空")
        @Schema(description = "年龄", example = "25", required = true)
        private Integer age;

        @NotNull(message = "身高不能为空")
        @Schema(description = "身高 (cm)", example = "175.5", required = true)
        private Double heightCm;

        @Schema(description = "体重 (kg)", example = "70.0")
        private Double weightKg;

        @Schema(description = "活动水平", example = "moderately_active")
        private String activityLevel;

        @Schema(description = "目标", example = "maintain")
        private String goal;
    }
}
