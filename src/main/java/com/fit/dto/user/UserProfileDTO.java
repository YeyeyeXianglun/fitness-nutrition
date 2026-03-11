package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户档案信息")
public class UserProfileDTO {

    @Schema(description = "档案 ID", example = "1")
    private Long id;

    @Schema(description = "用户 ID", example = "1")
    private Long userId;

    @Schema(description = "昵称", example = "张三")
    private String nickname;

    @Schema(description = "年龄", example = "25")
    private Integer age;

    @Schema(description = "性别", example = "MALE")
    private String gender;

    @Schema(description = "身高 (cm)", example = "175.5")
    private Double heightCm;

    @Schema(description = "体重 (kg)", example = "70.0")
    private Double weightKg;

    @Schema(description = "活动水平", example = "MODERATE")
    private String activityLevel;

    @Schema(description = "目标", example = "maintain")
    private String goal;
}
