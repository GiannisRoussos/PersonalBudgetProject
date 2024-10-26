package com.yourname.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;
    private Long budgetId;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private String category;
}
