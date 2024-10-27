package com.yourname.budget.repository;

import com.yourname.budget.model.Budget;
import com.yourname.budget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BudgetRepository extends JpaRepository<Budget,Long> {
    List<Budget> findByUser (User user);
}
