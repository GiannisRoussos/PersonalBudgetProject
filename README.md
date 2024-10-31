## Overview
The **Personal Budget Project** is a web application built using Java and Spring Boot, designed to help users manage their personal finances effectively. The application allows users to create budgets, track expenses and incomes, and visualize their financial health over time. You can check out the app here: https://personalbudgetproject.onrender.com/login

## Features
- **User Registration and Login**: Users can create an account and securely log in using Spring Security.
- **Budget Management**: Users can create multiple budgets, specifying amounts and categories.
- **Expense Tracking**: Users can add, view, and manage expenses linked to their budgets.
- **Income Tracking**: Users can add and manage income sources that contribute to their budgets.
- **Dynamic Calculations**: The application automatically calculates remaining budget amounts based on user-defined expenses and incomes.
- **Error Handling**: User-friendly error messages are displayed for authentication failures, ensuring a smooth user experience.

## Technologies Used
- **Java**: The primary programming language for the backend logic.
- **Spring Boot**: Framework used for building the web application, including RESTful services and security features.
- **Thymeleaf**: Template engine used for rendering HTML views.
- **HTML & CSS**: For structuring and styling the web pages.
- **Spring Security**: For authentication and authorization management.
- **Maven**: For dependency management and project build.

## Methods and Functionality
### Key Methods in Service Layer
- **UserService**
  - `registerUser(UserRegistrationDTO userDTO)`: Registers a new user, checking for existing usernames and emails, encrypting passwords, and saving user details.
  - `authenticate(UserLoginDTO userLoginDTO)`: Authenticates users by validating their credentials.

- **BudgetService**
  - `createBudget(BudgetDTO budgetDTO)`: Creates a new budget for the logged-in user.
  - `calculateTotalExpenses(UUID budgetId)`: Calculates total expenses for a given budget.
  - `calculateRemainingBudget(UUID budgetId)`: Computes the remaining amount for a budget based on total income and expenses.
