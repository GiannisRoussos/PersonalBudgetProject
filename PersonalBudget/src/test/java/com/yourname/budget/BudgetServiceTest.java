package com.yourname.budget;

import com.yourname.budget.model.Budget;
import com.yourname.budget.repository.BudgetRepository;
import com.yourname.budget.service.BudgetService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertNull;
@Slf4j
@SpringBootTest
public class BudgetServiceTest {

    @Autowired
    private BudgetService budgetService;

    private Budget savedBudget;

    @Test
    public void testSaveBudget() {
        Budget budget = new Budget();
        budget.setName("testdeleted");
        budget.setTotalAmount(BigDecimal.valueOf(1200));
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusMonths(1));
        savedBudget = budgetService.saveBudget(budget);
        // Επιβεβαίωση ότι αποθηκεύτηκε
        // Extra checks for debugging
        assertNotNull("Saved budget should not be null", savedBudget);
        assertNotNull("Saved budget ID should not be null", savedBudget.getId());
        // Log a success message
        log.info("Budget with ID {} saved successfully.", savedBudget.getId());
    }

    /*
    @AfterEach
    public void tearDown() {
        if (savedBudget != null) {
            budgetService.deleteBudget(savedBudget.getId());
        }

    }


    @Test
    void testDeleteBudget() {
        budgetService.deleteBudget(4L);
    }

     */
}