package com.expensetracker.controller;

import com.expensetracker.DTO.LoginRequest;
import com.expensetracker.DTO.UserWithTransactionsDTO;
import com.expensetracker.entity.User;
import com.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Get all users
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get all users with their transactions
    @GetMapping("/getAllwithTran")
    public ResponseEntity<List<UserWithTransactionsDTO>> getAllUsersWithTransactions() {
        List<UserWithTransactionsDTO> usersWithTransactions = userService.getAllUsersWithTransactions();
        return ResponseEntity.ok(usersWithTransactions);
    }

    // Update User
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("User updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete User
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Validate Login
    @PostMapping("/login")
    public ResponseEntity<String> validateLogin(@RequestBody LoginRequest loginRequest) {
        boolean isValid = userService.validateLogin(loginRequest);
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // Update budget limit
    @PutMapping("/{userId}/budget-limit")
    public ResponseEntity<String> updateBudgetLimit(
            @PathVariable int userId,
            @RequestBody Map<String, Object> requestBody) {

        if (!requestBody.containsKey("newBudgetLimit") || !requestBody.containsKey("enableAlert")) {
            return ResponseEntity.badRequest().body("newBudgetLimit and enableAlert are required");
        }

        try {
            Double newBudgetLimit = Double.valueOf(requestBody.get("newBudgetLimit").toString());
            boolean enableAlert = Boolean.parseBoolean(requestBody.get("enableAlert").toString());

            userService.updateBudgetLimit(userId, newBudgetLimit, enableAlert);
            return ResponseEntity.ok("Budget limit updated successfully.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid format for newBudgetLimit");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
