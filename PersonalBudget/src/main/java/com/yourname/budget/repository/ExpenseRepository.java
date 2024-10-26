package com.yourname.budget.repository;

import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByBudgetId(Long BudgetId);

    List<Expense> findByBudget(Budget budget);
}
