package com.yourname.budget.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Wrong username";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Wrong password or username";
        } else {
            errorMessage = "Invalid login attempt";
        }

        // Redirect to the login page with the error message as a query parameter
        response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
    }
}
