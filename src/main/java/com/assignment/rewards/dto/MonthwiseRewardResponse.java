package com.assignment.rewards.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MonthwiseRewardResponse {
    private String month;
    private int rewardPoints;
}
