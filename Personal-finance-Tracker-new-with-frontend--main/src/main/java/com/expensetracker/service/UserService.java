package com.expensetracker.service;

import com.expensetracker.DTO.LoginRequest;
import com.expensetracker.DTO.UserWithTransactionsDTO;
import com.expensetracker.Repository.TransactionRepository;
import com.expensetracker.Repository.UserRepository;
import com.expensetracker.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TransactionRepository transactionRepository;

    // Register a new user
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get all users (Read-only)
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    // Validate Login using LoginRequest DTO
    @Transactional(readOnly = true)
    public boolean validateLogin(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        return user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }

    // Update Budget Limit
    public void updateBudgetLimit(int userId, Double newBudgetLimit, boolean enableAlert) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        user.setBudgetLimit(newBudgetLimit);
        user.setBudgetAlertEnabled(enableAlert);
        userRepository.save(user);
    }

    // Get Budget Limit (Read-only)
    @Transactional(readOnly = true)
    public Double getBudgetLimit(int userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return user.getBudgetLimit();
    }

    // Get User by ID (Read-only)
    @Transactional(readOnly = true)
    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }
    
    @Transactional(readOnly = true)
    public List<UserWithTransactionsDTO> getAllUsersWithTransactions() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(user -> new UserWithTransactionsDTO(user, transactionRepository.findByUser_UserID(user.getUserID())))
                    .collect(Collectors.toList());
    }
}
