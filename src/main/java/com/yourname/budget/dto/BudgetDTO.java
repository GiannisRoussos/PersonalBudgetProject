package com.yourname.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private UUID userId;
    private String name;
    private BigDecimal totalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
