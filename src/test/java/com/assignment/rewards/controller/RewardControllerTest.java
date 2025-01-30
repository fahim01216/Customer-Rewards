package com.assignment.rewards.controller;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.exception.CustomerNotFoundException;
import com.assignment.rewards.exception.InvalidInputException;
import com.assignment.rewards.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RewardResponse mockResponse;

    @BeforeEach
    void setUp() {
        customerId = 1L;
        startDate = LocalDate.of(2023, 1, 1);
        endDate = LocalDate.of(2023, 12, 31);
        mockResponse = new RewardResponse(1L, "John Doe", null, null, 100);
    }

    @Test
    void calculateRewards_ValidInput_ReturnsRewardResponse() throws Exception {
        when(rewardService.calculateRewards(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/rewards/customer/{customerId}", customerId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }

    @Test
    void calculateRewards_StartDateAfterEndDate_ReturnsBadRequest() throws Exception {
        LocalDate invalidStartDate = LocalDate.of(2023, 12, 31);
        LocalDate invalidEndDate = LocalDate.of(2023, 1, 1);

        when(rewardService.calculateRewards(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new InvalidInputException("Start date cannot be after end date"));

        mockMvc.perform(get("/api/rewards/customer/{customerId}", customerId)
                        .param("startDate", invalidStartDate.toString())
                        .param("endDate", invalidEndDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Start date cannot be after end date"));
    }

    @Test
    void calculateRewards_CustomerNotFound_ReturnsNotFound() throws Exception {
        when(rewardService.calculateRewards(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/rewards/customer/{customerId}", 999L)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }
}
