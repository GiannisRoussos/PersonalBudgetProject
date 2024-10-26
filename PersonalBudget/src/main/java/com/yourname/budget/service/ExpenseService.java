package com.yourname.budget.service;

import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Expense;
import com.yourname.budget.repository.ExpenseRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
@Service
public class ExpenseService {

    @Autowired
    private final ExpenseRepository expenseRepository;

    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public Expense saveExpense(Expense expense) {
      return   expenseRepository.save(expense);
    }

    public List<Expense> findExpensesByBudgetId(Long budgetId) {
        return expenseRepository.findByBudgetId(budgetId);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public BigDecimal calculateTotalExpenses(Budget budget) {
        List<Expense> expenses = expenseRepository.findByBudget(budget);
                return expenses.stream()
                        .map(Expense::getAmount)
                        .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
