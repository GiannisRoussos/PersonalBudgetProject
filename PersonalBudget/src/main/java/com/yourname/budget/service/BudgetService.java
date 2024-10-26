package com.yourname.budget.service;

import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Expense;
import com.yourname.budget.model.Income;
import com.yourname.budget.model.User;
import com.yourname.budget.repository.BudgetRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@RequiredArgsConstructor
@Setter
@Getter
@Slf4j
@Service
public class BudgetService {
    @Autowired
    private final BudgetRepository budgetRepository;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    IncomeService incomeService;

    public List<Budget> findAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget findBudgetById(Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    public Budget saveBudget(Budget budget) {
        budget.setRemainingAmount(budget.getTotalAmount());
        return budgetRepository.save(budget);
    }

    public List<Budget> findBudgetsByUser(User user) {
        return budgetRepository.findByUser(user);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    public BigDecimal calculateRemainingBudget(Budget budget) {
        BigDecimal totalIncome = incomeService.calculateTotalIncome(budget);
        BigDecimal totalExpenses = expenseService.calculateTotalExpenses(budget);
        return budget.getTotalAmount()
                .add(totalIncome)
                .subtract(totalExpenses);
    }

    // Add an expense and update remaining amount
    public void addExpenseToBudget(Budget budget, Expense expense) {
        expenseService.saveExpense(expense); // Save the new expense
        BigDecimal updatedRemaining = calculateRemainingBudget(budget);
        budget.setRemainingAmount(updatedRemaining); // Update the remaining amount
        budgetRepository.save(budget); // Save the budget with the updated remaining amount
    }

    // Add an income and update remaining amount
    public void addIncomeToBudget(Budget budget, Income income) {
        incomeService.saveIncome(income); // Save the new income
        BigDecimal updatedRemaining = calculateRemainingBudget(budget);
        budget.setRemainingAmount(updatedRemaining); // Update the remaining amount
        budgetRepository.save(budget); // Save the budget with the updated remaining amount
    }

    public Budget updateRemainingAmountAndSave(Budget budget) {
        BigDecimal updatedRemaining = calculateRemainingBudget(budget);
        budget.setRemainingAmount(updatedRemaining);
        return budgetRepository.save(budget);
    }
}
