package com.assignment.rewards.dto;

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
    private int totalPoints;

    public RewardResponse(Long customerId, String customerName, List<TransactionResponse> transactions, int totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.transactions = transactions;
        this.totalPoints = totalPoints;
    }
}
