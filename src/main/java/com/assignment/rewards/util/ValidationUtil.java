package com.assignment.rewards.util;

import com.assignment.rewards.exception.InvalidInputException;

import java.time.LocalDate;

public class ValidationUtil {

    public static void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidInputException("Start date cannot be after end date");
        }
    }
}
