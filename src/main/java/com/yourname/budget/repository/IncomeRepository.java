package com.yourname.budget.repository;

import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository <Income,Long>{
    List<Income> findByBudget(Budget budget);
    List<Income> findByBudgetId(Long budgetId);
}
