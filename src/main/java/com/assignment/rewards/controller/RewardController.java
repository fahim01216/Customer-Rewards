package com.assignment.rewards.controller;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.exception.InvalidInputException;
import com.assignment.rewards.service.RewardService;
import com.assignment.rewards.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardsService;
    private static final Logger logger = LoggerFactory.getLogger(RewardController.class);

    public RewardController(RewardService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RewardResponse> calculateRewards(@PathVariable Long customerId,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        ValidationUtil.validateDates(startDate, endDate);
        logger.info("Fetching rewards for customerId={} from {} to {}", customerId, startDate, endDate);
        return ResponseEntity.ok(rewardsService.calculateRewards(customerId, startDate, endDate));
    }
}

