package com.fit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long id;
    private Long userId;
    private String gender;
    private Integer age;
    private Double heightCm;
    private Double weightKg;
    private String activityLevel;
    private String goal;
}
