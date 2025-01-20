package com.assignment.rewards.service;

import org.springframework.stereotype.Component;

@Component
public class RewardPointsCalculator implements PointsCalculator {
    @Override
    public int calculatePoints(int amount) {
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
