package com.yourname.budget.controller;

import com.yourname.budget.dto.BudgetDTO;
import com.yourname.budget.model.User;
import com.yourname.budget.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import com.yourname.budget.model.Budget;
import com.yourname.budget.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


@Controller
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    // Show the form for adding a new budget
    @GetMapping("/addBudget/{userId}")
    public String showAddBudgetForm(@PathVariable("userId") UUID userId,
                                    Model model) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setUserId(userId);  // Link the budget to the logged-in user
        model.addAttribute("budget", budgetDTO);
        return "budgets/addBudget";
    }


    //Fetch the User entity based on the username
    @GetMapping("/budgets")
    public String getUserBudgets(Model model) {
        // Get the logged-in user's authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the user from the database
        User user = userService.findUserByUsername(username);

        // Fetch only the budgets that belong to this user
        List<Budget> userBudgets = budgetService.findBudgetsByUser(user);

        // Add the user's name and their budgets to the model
        model.addAttribute("username", user.getUsername());
        model.addAttribute("budgets", userBudgets);
        model.addAttribute("userId", user.getId());

        return "budgets/budgetList";  // Return the Thymeleaf template for the budget list
    }

    @PostMapping("/addBudget")
    public String addBudget(@ModelAttribute("budget") BudgetDTO budgetDTO, Principal principal) {
        // Fetch the currently logged-in user
        User user = userService.findUserByUsername(principal.getName());

        // Create a new Budget entity and set its fields from the DTO
        Budget budget = new Budget();
        budget.setName(budgetDTO.getName());
        budget.setTotalAmount(budgetDTO.getTotalAmount());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());

        // Associate the budget with the user
        budget.setUser(user);

        // Save the budget
        Budget savedBudget = budgetService.saveBudget(budget);

        // If you need to do something with the saved budget (e.g., use its ID or updated fields)
        Long savedBudgetId = savedBudget.getId();
        System.out.println("Saved budget with ID: " + savedBudgetId);

        // Redirect to the budget list page after successful creation
        return "redirect:/budgets";
    }

    @PostMapping("/deleteBudget")
    public String deleteBudget(@RequestParam("id") Long budgetId) {
        // Find the budget by its ID
        Budget budget = budgetService.findBudgetById(budgetId);

        // Delete the budget if it exists
        if (budget != null) {
            budgetService.deleteBudget(budgetId);
            System.out.println("Deleted budget with ID: " + budgetId);
        }

        // Redirect back to the budget list after deletion
        return "redirect:/budgets";
    }

}


