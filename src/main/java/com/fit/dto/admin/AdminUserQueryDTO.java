package com.fit.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员用户查询条件")
public class AdminUserQueryDTO {

    @Schema(description = "搜索关键字（用户名/邮箱）", example = "zhangsan")
    private String keyword;

    @Schema(description = "角色过滤", example = "USER")
    private String role;

    @Schema(description = "状态过滤", example = "ACTIVE")
    private String status;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;
}
