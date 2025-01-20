package com.assignment.rewards;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.dto.TransactionResponse;
import com.assignment.rewards.entity.Customer;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.exception.CustomerNotFoundException;
import com.assignment.rewards.repository.CustomerRepository;
import com.assignment.rewards.repository.TransactionRepository;
import com.assignment.rewards.service.PointsCalculator;
import com.assignment.rewards.service.RewardService;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("John Doe");

        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(120);
        transaction1.setDate(LocalDate.of(2023, 1, 1));

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(80);
        transaction2.setDate(LocalDate.of(2023, 1, 2));
    }

    @Test
    void calculateRewards_withValidCustomerIdAndTransactions_returnsRewardResponse() {
        // Mock dependencies
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        when(pointsCalculator.calculatePoints(120)).thenReturn(90); // Custom logic
        when(pointsCalculator.calculatePoints(80)).thenReturn(30);  // Custom logic

        // Call the method under test
        RewardResponse response = rewardService.calculateRewards(1L, null, null);

        // Verify interactions and assertions
        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerId(1L);
        verify(pointsCalculator).calculatePoints(120);
        verify(pointsCalculator).calculatePoints(80);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(2, response.getTransactions().size());
        assertEquals(120, response.getTransactions().get(0).getAmount());
        assertEquals(90, response.getTransactions().get(0).getPoints());
        assertEquals(80, response.getTransactions().get(1).getAmount());
        assertEquals(30, response.getTransactions().get(1).getPoints());
        assertEquals(120, response.getTotalPoints());
    }

    @Test
    void calculateRewards_withDateFilter_returnsFilteredTransactions() {
        // Mock dependencies
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndDateBetween(1L, startDate, endDate))
                .thenReturn(Collections.singletonList(transaction1));

        when(pointsCalculator.calculatePoints(120)).thenReturn(90);

        // Call the method under test
        RewardResponse response = rewardService.calculateRewards(1L, startDate, endDate);

        // Verify interactions and assertions
        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerIdAndDateBetween(1L, startDate, endDate);
        verify(pointsCalculator).calculatePoints(120);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals(1, response.getTransactions().size());
        assertEquals(120, response.getTransactions().get(0).getAmount());
        assertEquals(90, response.getTransactions().get(0).getPoints());
        assertEquals(90, response.getTotalPoints());
    }

    @Test
    void calculateRewards_withNonExistingCustomer_throwsException() {
        // Mock dependencies
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the method under test and assert exception
        assertThrows(CustomerNotFoundException.class, () ->
                rewardService.calculateRewards(1L, null, null));

        // Verify interactions
        verify(customerRepository).findById(1L);
        verifyNoInteractions(transactionRepository, pointsCalculator);
    }

    @Test
    void calculateRewards_withNoTransactions_returnsZeroPoints() {
        // Mock dependencies
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        // Call the method under test
        RewardResponse response = rewardService.calculateRewards(1L, null, null);

        // Verify interactions and assertions
        verify(customerRepository).findById(1L);
        verify(transactionRepository).findByCustomerId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertTrue(response.getTransactions().isEmpty());
        assertEquals(0, response.getTotalPoints());
    }
}
