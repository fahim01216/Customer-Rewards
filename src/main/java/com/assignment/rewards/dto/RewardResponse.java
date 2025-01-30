package com.assignment.rewards.dto;

import com.assignment.rewards.entity.MonthwiseReward;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Setter
@Getter
@Data
public class RewardResponse {

    private Long customerId;
    private String customerName;
    private List<TransactionResponse> transactions;
    private List<MonthwiseRewardResponse> monthwiseRewards;
    private int totalPoints;

    public RewardResponse(Long customerId, String customerName, List<TransactionResponse> transactions, List<MonthwiseRewardResponse> monthwiseRewards, int totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.transactions = transactions;
        this.monthwiseRewards = monthwiseRewards;
        this.totalPoints = totalPoints;
    }
}
