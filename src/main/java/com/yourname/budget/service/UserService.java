package com.yourname.budget.service;

import com.yourname.budget.dto.UserRegistrationDTO;
import com.yourname.budget.model.User;
import com.yourname.budget.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@RequiredArgsConstructor
@Setter
@Getter
@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public User findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    public void registerUser(UserRegistrationDTO userDTO) throws Exception {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new Exception("Username already exists");
        }
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new Exception("Email already exists");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encrypt password
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);  // Fetch user from the database
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert your User entity to Spring's UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
