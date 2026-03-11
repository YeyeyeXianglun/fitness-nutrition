package com.fit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "代谢计算结果")
public class MetabolismDTO {

    @Schema(description = "基础代谢 BMR (kcal/day)", example = "1600.5")
    private Double bmr;

    @Schema(description = "活动系数", example = "1.55")
    private Double activityFactor;

    @Schema(description = "每日总消耗 TDEE (kcal/day)", example = "2480.8")
    private Double tdee;
}

