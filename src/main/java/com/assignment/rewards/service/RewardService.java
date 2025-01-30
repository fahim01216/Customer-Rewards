package com.assignment.rewards.service;

import com.assignment.rewards.dto.MonthwiseRewardResponse;
import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.dto.TransactionResponse;
import com.assignment.rewards.entity.Customer;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.exception.CustomerNotFoundException;
import com.assignment.rewards.repository.CustomerRepository;
import com.assignment.rewards.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private CustomerRepository customerRepository = null;
    private final TransactionRepository transactionRepository;
    private final PointsCalculator rewardsPointCalculator;
    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);

    public RewardService(CustomerRepository customerRepository ,TransactionRepository transactionRepository, PointsCalculator rewardsPointCalculator) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.rewardsPointCalculator = rewardsPointCalculator;
    }

    @Transactional
    public RewardResponse calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() ->
                            new CustomerNotFoundException("Customer not found with ID: " + customerId));
            logger.debug("Customer data retrieved: {}", customer);

            List<Transaction> transactions;
            try {
                if (startDate != null && endDate != null) {
                    transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
                } else {
                    transactions = transactionRepository.findByCustomerId(customerId);
                }
            } catch (DataAccessException ex) {
                logger.error("No transactions found for the given customer ID: {}", customerId, ex);
                throw new CustomerNotFoundException("No transactions found for the given customer ID " + customerId);
            }
            logger.debug("Transactions retrieved: {}", transactions);

            Map<YearMonth, Integer> monthwisePoints = new HashMap<>();
            List<TransactionResponse> transactionResponses = new ArrayList<>();
            int totalPoints = 0;

            for (Transaction transaction : transactions) {
                int points = rewardsPointCalculator.calculatePoints(transaction.getAmount());
                totalPoints += points;

                YearMonth transactionMonth = YearMonth.from(transaction.getDate());
                monthwisePoints.put(transactionMonth, monthwisePoints.getOrDefault(transactionMonth, 0) + points);

                transactionResponses.add(new TransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getDate(), points));
            }

            List<MonthwiseRewardResponse> monthwiseRewards = customer.getMonthwiseRewards().stream()
                    .map(mr -> new MonthwiseRewardResponse(mr.getMonth(), mr.getRewardPoints()))
                    .collect(Collectors.toList());


            return new RewardResponse(customer.getId(), customer.getCustomerName(), transactionResponses, monthwiseRewards, totalPoints);

        } catch (CustomerNotFoundException e) {
            logger.error("Customer not found error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while calculating rewards for customer ID {}: {}", customerId, e.getMessage());
            throw e;
        }
    }
}
