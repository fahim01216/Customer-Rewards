package com.assignment.rewards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
@Getter
@Setter
@Entity(name = "monthwise_reward")
public class MonthwiseReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "month")
    private String month;
    @Column(name = "reward_points")
    private int rewardPoints;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public MonthwiseReward(Customer customer, YearMonth yearMonth, int rewardPoints) {
        this.customer = customer;
        this.month = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")); // Convert YearMonth to String
        this.rewardPoints = rewardPoints;
    }

    public YearMonth getYearMonth() {
        return YearMonth.parse(this.month, DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.month = yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
