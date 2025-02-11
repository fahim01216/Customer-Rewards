package com.assignment.rewards.service;

import com.assignment.rewards.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class RewardPointsCalculator {
    public int calculatePoints(Transaction transaction) {
        int amount = transaction.getAmount();
        int points = 0;
        if (amount > 100) {
            points += 2 * (amount - 100);
            amount = 100;
        }
        if (amount > 50) {
            points += amount - 50;
        }
        return points;
    }
}
