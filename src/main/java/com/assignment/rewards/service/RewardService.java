package com.assignment.rewards.service;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.dto.TransactionResponse;
import com.assignment.rewards.entity.Customer;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.exception.CustomerNotFoundException;
import com.assignment.rewards.repository.CustomerRepository;
import com.assignment.rewards.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
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

    public RewardResponse calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() ->
                            new CustomerNotFoundException("Customer not found with ID: " + customerId));
            logger.debug("Customer data retrieved: {}", customer);

            List<Transaction> transactions;
            if (startDate != null && endDate != null) {
                transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
            } else {
                transactions = transactionRepository.findByCustomerId(customerId);
            }
            logger.debug("Transactions retrieved: {}", transactions);

            final int[] totalPoints = {0};
            logger.info("Starting reward calculation for customerId={} with startDate={} and endDate={}", customerId, startDate, endDate);

            List<TransactionResponse> transactionResponses = transactions.stream()
                    .map(transaction -> {
                        int points = rewardsPointCalculator.calculatePoints(transaction.getAmount());
                        totalPoints[0] += points;
                        return new TransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getDate(), points);
                    })
                    .collect(Collectors.toList());

            return new RewardResponse(customer.getId(), customer.getCustomerName(), transactionResponses, totalPoints[0]);
        } catch (Exception e) {
            logger.error("Error occurred while calculating rewards for customer ID {}: {}", customerId, e.getMessage());
            throw e;
        }
    }

//    public int calculatePoints(int amount) {
//        int points = 0;
//        if (amount > 100) {
//            points += 2 * (amount - 100);
//            amount = 100;
//        }
//        if (amount > 50) {
//            points += amount - 50;
//        }
//        return points;
//    }
}
