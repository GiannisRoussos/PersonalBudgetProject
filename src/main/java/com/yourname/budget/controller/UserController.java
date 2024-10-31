package com.yourname.budget.controller;

import com.yourname.budget.dto.UserRegistrationDTO;
import com.yourname.budget.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "");
        model.addAttribute("error", "");
        return "registrationForm";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "loginForm";
    }

    // Mapping for the logout process
    @GetMapping("/logout")
    public String logoutUser() {
        return "redirect:/login"; // Redirect to the login form after logout
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("email") String email,
            Model model) {

        // Create a DTO from form parameters
        UserRegistrationDTO userDTO = new UserRegistrationDTO(username, password, confirmPassword, email);

        // Check if password and confirmPassword match
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "registrationForm";
        }

        try {
            // Register the user using the service
            userService.registerUser(userDTO);
            model.addAttribute("message", "User registered successfully!");
            return "redirect:/login"; // Redirect to the login page after successful registration
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "registrationForm"; // Return to the registration form on error
        }
    }

}
