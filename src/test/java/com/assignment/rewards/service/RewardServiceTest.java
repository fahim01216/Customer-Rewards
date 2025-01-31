package com.assignment.rewards.service;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.entity.Customer;
import com.assignment.rewards.entity.MonthwiseReward;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.exception.CustomerNotFoundException;
import com.assignment.rewards.exception.InvalidInputException;
import com.assignment.rewards.repository.CustomerRepository;
import com.assignment.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private PointsCalculator pointsCalculator;
    @InjectMocks
    private RewardService rewardService;

    private Customer customer;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;
    private Transaction transaction4;

    private MonthwiseReward monthwiseReward1;
    private MonthwiseReward monthwiseReward2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("Sunny Kumar");

        // Sample transactions with different months
        transaction1 = new Transaction(1L, 120, LocalDate.of(2023, 1, 5), customer);
        transaction2 = new Transaction(2L, 80, LocalDate.of(2023, 1, 15), customer);
        transaction3 = new Transaction(3L, 150, LocalDate.of(2023, 2, 10), customer);
        transaction4 = new Transaction(4L, 50, LocalDate.of(2023, 2, 20), customer);
    }

    @Test
    void calculateRewards_withValidCustomerIdAndTransactions_returnsRewardResponse() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(Arrays.asList(transaction1, transaction2, transaction3, transaction4));

        when(pointsCalculator.calculatePoints(120)).thenReturn(90);
        when(pointsCalculator.calculatePoints(80)).thenReturn(30);
        when(pointsCalculator.calculatePoints(150)).thenReturn(150);
        when(pointsCalculator.calculatePoints(50)).thenReturn(0);

        RewardResponse response = rewardService.calculateRewards(1L, null, null);

        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("Sunny Kumar", response.getCustomerName());
        assertEquals(4, response.getTransactions().size());
        assertEquals(270, response.getTotalPoints()); // 90 + 30 + 150 + 0

    }


    @Test
    void calculateRewards_withDateFilter_returnsFilteredTransactions() {

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndDateBetween(1L, startDate, endDate))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        when(pointsCalculator.calculatePoints(120)).thenReturn(90);
        when(pointsCalculator.calculatePoints(80)).thenReturn(30);

        RewardResponse response = rewardService.calculateRewards(1L, startDate, endDate);

        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerIdAndDateBetween(1L, startDate, endDate);

        assertNotNull(response);
        assertEquals(120, response.getTotalPoints()); // 90 + 30
    }

    @Test
    void calculateRewards_withInvalidDateRange_throwsInvalidInputException() {
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2023, 01, 10);

        assertThrows(InvalidInputException.class, () ->
                rewardService.calculateRewards(1L, startDate, endDate));
    }

    @Test
    void calculateRewards_withNonExistingCustomer_throwsException() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewards(1L, null, null));

        verify(customerRepository).findById(1L);
        verifyNoInteractions(transactionRepository, pointsCalculator);
    }

    @Test
    void calculateRewards_withNoTransactions_returnsZeroPoints() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        RewardResponse response = rewardService.calculateRewards(1L, null, null);

        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerId(1L);

        assertNotNull(response);
        assertEquals(0, response.getTotalPoints());
    }

    @Test
    void calculateRewards_withThresholdAmounts_correctlyCalculatesPoints() {
        Transaction transaction50 = new Transaction(5L, 50, LocalDate.of(2023, 3, 10), customer);
        Transaction transaction100 = new Transaction(6L, 100, LocalDate.of(2023, 3, 15), customer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(transaction50, transaction100));

        when(pointsCalculator.calculatePoints(50)).thenReturn(0);
        when(pointsCalculator.calculatePoints(100)).thenReturn(50);

        RewardResponse response = rewardService.calculateRewards(1L, null, null);

        verify(pointsCalculator).calculatePoints(50);
        verify(pointsCalculator).calculatePoints(100);

        assertEquals(50, response.getTotalPoints());
    }

    @Test
    void calculatePoints_correctlyCalculatesRewardPoints() {
        RewardPointsCalculator rewardPointsCalculator  = new RewardPointsCalculator();

        assertEquals(90, rewardPointsCalculator.calculatePoints(120), "Expected 90 reward points for 120 transaction");
        assertEquals(30, rewardPointsCalculator.calculatePoints(80), "Expected 30 reward points for 80 transaction");
        assertEquals(150, rewardPointsCalculator.calculatePoints(150), "Expected 150 reward points for 150 transaction");
        assertEquals(0, rewardPointsCalculator.calculatePoints(50), "Expected 0 reward points for 50 transaction");
        assertEquals(0, rewardPointsCalculator.calculatePoints(40), "Expected 0 reward points for 40 transaction");
    }
}
