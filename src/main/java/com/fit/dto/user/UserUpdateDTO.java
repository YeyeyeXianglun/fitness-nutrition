package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息修改请求")
public class UserUpdateDTO {

    @Schema(description = "邮箱", example = "newemail@example.com")
    private String email;

    @Schema(description = "手机号", example = "13900139000")
    private String phone;
}
