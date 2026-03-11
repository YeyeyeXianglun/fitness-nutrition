package com.fit.dto.auth;

import com.fit.dto.user.UserDTO;
import com.fit.dto.user.UserProfileDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token 类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "用户信息")
    private UserDTO user;

    @Schema(description = "用户档案信息")
    private UserProfileDTO profile;
}
