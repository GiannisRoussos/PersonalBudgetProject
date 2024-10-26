package com.yourname.budget.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/register").permitAll();  // Allow access to login and register pages
                    auth.anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/loginForm.html")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/budgets")
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL for logging out
                        .logoutSuccessUrl("/login")  // Redirect to login page after logout
                        .invalidateHttpSession(true)  // Invalidate the session
                        .deleteCookies("JSESSIONID"))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
