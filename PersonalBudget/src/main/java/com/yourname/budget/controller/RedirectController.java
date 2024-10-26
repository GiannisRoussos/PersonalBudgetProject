package com.yourname.budget.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Secure after logout the user cannot reload the previous page
@Controller
@RequestMapping("/loginForm.html")
public class RedirectController {

    @GetMapping
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // Redirect authenticated users away from the login page
            return "redirect:/budgetList";
        }
        return "loginForm";
    }

    @GetMapping("/login-error")
    public String loginError(@RequestParam("errorType") String errorType, Model model) {
        String errorMessage;
        switch (errorType) {
            case "username":
                errorMessage = "Wrong username";
                break;
            case "password":
                errorMessage = "Wrong password";
                break;
            default:
                errorMessage = "Login failed. Please try again.";
        }

        model.addAttribute("error", errorMessage);
        return "loginForm";  // Renders the login page with the error message
    }
}
