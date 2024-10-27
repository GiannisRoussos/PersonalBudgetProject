package com.yourname.budget.controller;

import com.yourname.budget.dto.IncomeDTO;
import com.yourname.budget.model.Budget;
import com.yourname.budget.model.Income;
import com.yourname.budget.model.User;
import com.yourname.budget.service.BudgetService;
import com.yourname.budget.service.IncomeService;
import com.yourname.budget.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class IncomeController {

    @Autowired
    private IncomeService incomeService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private UserService userService;

    // Show the form for adding a new expense to a specific budget
    @GetMapping("/addIncome/{budgetId}")
    public String showAddIncomeForm(@PathVariable("budgetId") Long budgetId, Model model, Principal principal) {
        // Fetch the budget by ID and check if it belongs to the logged-in user
        Budget budget = budgetService.findBudgetById(budgetId);
        User loggedInUser = userService.findUserByUsername(principal.getName());
        budget.getUser().getId().equals(loggedInUser.getId());
        IncomeDTO incomeDTO = new IncomeDTO();
        incomeDTO.setBudgetId(budgetId);
        model.addAttribute("income", incomeDTO);
        return "incomes/addIncome";
    }

    // Show the incomes list for a specific budget
    @GetMapping("/viewIncomes/{budgetId}")
    public String viewIncomes(@PathVariable("budgetId") Long budgetId, Model model, Principal principal) {
        // Get the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Fetch the budget and verify ownership
        Budget budget = budgetService.findBudgetById(budgetId);

        // Retrieve incomes for the specified budget
        List<Income> incomes = incomeService.findIncomesByBudgetId(budgetId);

        // Add attributes to model for the Thymeleaf template
        model.addAttribute("incomes", incomes);
        model.addAttribute("budgetId", budgetId);
        model.addAttribute("budgetName", budget.getName());
        return "incomes/incomeList"; // Return the view for the income list
    }

    @GetMapping("/editIncome")
    public String editIncome(@RequestParam("id") Long id, Model model) {
        Income income = incomeService.findIncomeById(id);
        model.addAttribute("income", income);
        model.addAttribute("budgetId", income.getBudget().getId());
        return "incomes/editIncome";
    }

    // Handle the submission of the add income form
    @PostMapping("/addIncome")
    public String addIncome(@ModelAttribute("income") IncomeDTO incomeDTO, Principal principal) {
        // Fetch the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Find the associated budget
        Budget budget = budgetService.findBudgetById(incomeDTO.getBudgetId());

        // Create and save the new income
        if (budget.getUser().getId().equals(user.getId())) {
            Income income = new Income();
            income.setAmount(incomeDTO.getAmount());
            income.setSource(incomeDTO.getSource());
            income.setDate(incomeDTO.getDate());
            income.setCategory(incomeDTO.getCategory());
            income.setBudget(budget);

            // Save the income
            incomeService.saveIncome(income);

            // Update the remaining budget amount (you may want to implement a method for this)
            budgetService.updateRemainingAmountAndSave(budget);
        }

        // Redirect to the budget list page after adding the income
        return "redirect:/budgets";
    }

    //Update an Income after Editing
    @PostMapping("/addIncomeToBudget")
    public String updateIncome(@ModelAttribute("income") IncomeDTO incomeDTO, Principal principal) {
        // Fetch the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Find the associated budget
        Budget budget = budgetService.findBudgetById(incomeDTO.getBudgetId());

        // Check if the budget belongs to the user
        if (budget.getUser().getId().equals(user.getId())) {
            // Fetch the existing income
            Income existingIncome = incomeService.findIncomeById(incomeDTO.getId());
            if (existingIncome != null && existingIncome.getBudget().getId().equals(budget.getId())) {
                // Update the expense fields
                existingIncome.setAmount(incomeDTO.getAmount());
                existingIncome.setSource(incomeDTO.getSource());
                existingIncome.setDate(incomeDTO.getDate());
                existingIncome.setCategory(incomeDTO.getCategory());

                // Save the updated expense
                incomeService.saveIncome(existingIncome);

                // Update the remaining budget amount if necessary
                budgetService.updateRemainingAmountAndSave(budget);
            }
        }

        // Redirect to the income list page after updating the expense
        return "redirect:/viewIncomes/" + budget.getId();
    }

    @PostMapping("/deleteIncome")
    public String deleteIncome(@RequestParam("id") Long id,
                               @RequestParam("budgetId") Long budgetId) {
        incomeService.deleteIncome(id);
        Budget budget = budgetService.findBudgetById(budgetId);
        BigDecimal newRemainingAmount = budgetService.calculateRemainingBudget(budget);
        budget.setRemainingAmount(newRemainingAmount);
        budgetService.saveBudget(budget);
        return "redirect:/viewIncomes/" + budgetId;
    }
}
