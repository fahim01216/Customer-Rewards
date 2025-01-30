package com.assignment.rewards.controller;

import com.assignment.rewards.controller.RewardController;
import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.exception.InvalidInputException;
import com.assignment.rewards.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateRewards_ValidInput_ReturnsRewardResponse() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        RewardResponse mockResponse = new RewardResponse();
        when(rewardService.calculateRewards(customerId, startDate, endDate)).thenReturn(mockResponse);

        ResponseEntity<RewardResponse> response = rewardController.calculateRewards(customerId, startDate, endDate);

        assertNotNull(response);
        assertEquals(mockResponse, response.getBody());
        verify(rewardService, times(1)).calculateRewards(customerId, startDate, endDate);
    }

    @Test
    void calculateRewards_StartDateAfterEndDate_ThrowsInvalidInputException() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2023, 1, 1);

        assertThrows(InvalidInputException.class, () ->
                rewardController.calculateRewards(customerId, startDate, endDate));
    }
}