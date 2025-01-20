package com.assignment.rewards.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Data
public class TransactionResponse {

    private Long transactionId;
    private int amount;
    private LocalDate date;
    private int points;

    public TransactionResponse(Long transactionId, int amount, LocalDate date, int points) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.date = date;
        this.points = points;
    }
}
