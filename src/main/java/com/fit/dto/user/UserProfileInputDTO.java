package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户档案输入")
public class UserProfileInputDTO {

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
