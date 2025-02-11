package com.assignment.rewards.service;

import com.assignment.rewards.dto.RewardResponse;

import java.time.LocalDate;

public interface RewardService {
    RewardResponse calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate);
}

