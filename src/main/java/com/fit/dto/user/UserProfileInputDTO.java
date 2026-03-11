package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "用户档案输入")
public class UserProfileInputDTO {

    @Schema(description = "性别", example = "MALE", allowableValues = {"MALE", "FEMALE", "OTHER"})
    private String gender;

    @Min(1) @Max(150)
    @Schema(description = "年龄", example = "25")
    private Integer age;

    @Min(50) @Max(250)
    @Schema(description = "身高(厘米)", example = "170")
    private Integer heightCm;

    @DecimalMin("20") @DecimalMax("500")
    @Schema(description = "体重(千克)", example = "65.5")
    private Double weightKg;

    @Schema(description = "活动等级（有氧强度）",
            example = "cardio_3",
            allowableValues = {"no_cardio", "cardio_3", "cardio_5",
                    "sedentary", "lightly_active", "moderately_active", "very_active", "extra_active"})
    private String activityLevel;

    @Schema(description = "目标", example = "maintain",
            allowableValues = {"lose_weight", "maintain", "gain_weight"})
    private String goal;
}
