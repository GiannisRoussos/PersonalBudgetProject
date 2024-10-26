package com.yourname.budget.controller;

import com.yourname.budget.dto.ExpenseDTO;
import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Expense;
import com.yourname.budget.model.User;
import com.yourname.budget.service.BudgetService;
import com.yourname.budget.service.ExpenseService;
import com.yourname.budget.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private UserService userService;

    // Show the form for adding a new expense to a specific budget
    @GetMapping("/addExpense/{budgetId}")
    public String showAddExpenseForm(@PathVariable("budgetId") Long budgetId, Model model, Principal principal) {
        // Fetch the budget by ID and check if it belongs to the logged-in user
        Budget budget = budgetService.findBudgetById(budgetId);
        User loggedInUser = userService.findUserByUsername(principal.getName());

        budget.getUser().getId().equals(loggedInUser.getId());
        // If the budget belongs to the logged-in user, show the add expense form
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setBudgetId(budgetId);
        model.addAttribute("expense", expenseDTO);
        return "expenses/addExpense";  // Return the add expense form view

    }

    @GetMapping("/viewExpenses/{budgetId}")
    public String viewExpenses(@PathVariable("budgetId") Long budgetId, Model model, Principal principal) {
        // Get the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Fetch the budget and verify ownership
        Budget budget = budgetService.findBudgetById(budgetId);

        // Retrieve expenses for the specified budget
        List<Expense> expenses = expenseService.findExpensesByBudgetId(budgetId);

        // Add attributes to model for the Thymeleaf template
        model.addAttribute("expenses", expenses);
        model.addAttribute("budgetId", budgetId);
        model.addAttribute("budgetName", budget.getName());
        model.addAttribute("userId", user.getId());

        return "expenses/expenseList"; // Return the view for the expense list
    }

    @GetMapping("/editExpense")
    public String editExpense(@RequestParam("id") Long id, Model model) {
        Expense expense = expenseService.findExpenseById(id);
        model.addAttribute("expense", expense);
        model.addAttribute("budgetId", expense.getBudget().getId());
        return "expenses/editExpense";
    }

    //Add new expense to budget
    @PostMapping("/addExpense")
    public String addExpense(@ModelAttribute("expense") ExpenseDTO expenseDTO, Principal principal) {
        // Fetch the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Find the associated budget
        Budget budget = budgetService.findBudgetById(expenseDTO.getBudgetId());

        // Create and save the new expense
        if (budget.getUser().getId().equals(user.getId())) {
            Expense expense = new Expense();
            expense.setAmount(expenseDTO.getAmount());
            expense.setDescription(expenseDTO.getDescription());
            expense.setDate(expenseDTO.getDate());
            expense.setCategory(expenseDTO.getCategory());
            expense.setBudget(budget);

            // Save the expense
            expenseService.saveExpense(expense);

            // Update the remaining budget amount
            budgetService.updateRemainingAmountAndSave(budget);
        }

        // Redirect to the budget list page after adding the expense
        return "redirect:/budgets";
    }

    //Update an Expense after Editing
    @PostMapping("/addExpenseToBudget")
    public String updateExpense(@ModelAttribute("expense") ExpenseDTO expenseDTO, Principal principal) {
        // Fetch the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Find the associated budget
        Budget budget = budgetService.findBudgetById(expenseDTO.getBudgetId());

        // Check if the budget belongs to the user
        if (budget.getUser().getId().equals(user.getId())) {
            // Fetch the existing expense
            Expense existingExpense = expenseService.findExpenseById(expenseDTO.getId());
            if (existingExpense != null && existingExpense.getBudget().getId().equals(budget.getId())) {
                // Update the expense fields
                existingExpense.setAmount(expenseDTO.getAmount());
                existingExpense.setDescription(expenseDTO.getDescription());
                existingExpense.setDate(expenseDTO.getDate());
                existingExpense.setCategory(expenseDTO.getCategory());

                // Save the updated expense
                expenseService.saveExpense(existingExpense);

                // Update the remaining budget amount if necessary
                budgetService.updateRemainingAmountAndSave(budget);
            }
        }

        // Redirect to the expense list page after updating the expense
        return "redirect:/viewExpenses/" + budget.getId(); // Assuming this redirects to the expense list for the budget
    }

    @PostMapping("/deleteExpense")
    public String deleteExpense(@RequestParam("id") Long id,
                                @RequestParam("budgetId") Long budgetId) {
        expenseService.deleteExpense(id);
        Budget budget = budgetService.findBudgetById(budgetId);
        BigDecimal newRemainingAmount = budgetService.calculateRemainingBudget(budget);
        budget.setRemainingAmount(newRemainingAmount);
        budgetService.saveBudget(budget);
        return "redirect:/viewExpenses/" + budgetId;
    }

}
