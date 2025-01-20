package com.assignment.rewards.dto;

import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Data
public class ErrorResponse {

    private int statusCode;
    private String message;
    private LocalDate timestamp;

    public ErrorResponse(int statusCode, String message, LocalDate timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }
}
