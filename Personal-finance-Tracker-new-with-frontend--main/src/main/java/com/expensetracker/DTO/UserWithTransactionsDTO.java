package com.expensetracker.DTO;

import com.expensetracker.entity.TransactionSummary;
import com.expensetracker.entity.User;

import java.util.List;

public class UserWithTransactionsDTO {
    private User user;
    private List<TransactionSummary> transactions;

    // Constructors, getters, and setters
    public UserWithTransactionsDTO(User user, List<TransactionSummary> transactions) {
        this.user = user;
        this.transactions = transactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TransactionSummary> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionSummary> transactions) {
        this.transactions = transactions;
    }
}
