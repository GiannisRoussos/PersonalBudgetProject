package com.yourname.budget.service;


import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Income;
import com.yourname.budget.repository.IncomeRepository;
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
public class IncomeService {
    @Autowired
    private final IncomeRepository incomeRepository;

    public List<Income> findAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income findIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<Income> findIncomesByBudgetId(Long budgetId){
        return incomeRepository.findByBudgetId(budgetId);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    public BigDecimal calculateTotalIncome(Budget budget) {
        List<Income> incomes = incomeRepository.findByBudget(budget);
        return incomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
