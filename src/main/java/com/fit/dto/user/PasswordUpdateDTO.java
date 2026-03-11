package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "密码修改请求")
public class PasswordUpdateDTO {

    @NotBlank(message = "原密码不能为空")
    @Schema(description = "原密码", example = "123456", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "密码长度不能少于 6 位")
    @Schema(description = "新密码", example = "new123456", required = true)
    private String newPassword;
}
