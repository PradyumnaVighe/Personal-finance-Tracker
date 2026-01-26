package com.expensetracker.service;

import com.expensetracker.Repository.TransactionRepository;
import com.expensetracker.Repository.UserRepository;
import com.expensetracker.entity.CategoryType;
import com.expensetracker.entity.TransactionSummary;
import com.expensetracker.entity.TransactionType;
import com.expensetracker.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a transaction
    @Transactional
    public void addTransaction(int userId, TransactionSummary transaction) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Double totalSpent = transactionRepository.getTotalSpentByUser(userId);
        totalSpent = (totalSpent != null) ? totalSpent : 0.0;

        if (user.isBudgetAlertEnabled() && totalSpent + transaction.getAmount() > user.getBudgetLimit()) {
            throw new RuntimeException("Budget exceeded! Your spending limit is ₹" + user.getBudgetLimit());
        }

        transaction.setUser(user);
        transactionRepository.save(transaction);
    }

    // Get all transactions for a user
    @Transactional(readOnly = true)
    public List<TransactionSummary> getAllTransactions(int userId) {
        return transactionRepository.findByUser_UserID(userId);
    }

    // Update a transaction
    @Transactional
    public void updateTransaction(TransactionSummary transaction) {
        TransactionSummary existingTransaction = transactionRepository.findById(transaction.getTraID())
                                                                     .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + transaction.getTraID()));

        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setType(transaction.getType());
        existingTransaction.setTransactionDate(transaction.getTransactionDate());

        transactionRepository.save(existingTransaction);
    }

    // Delete a transaction
    @Transactional
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    // Filter Transactions with Optional Parameters
    @Transactional(readOnly = true)
    public List<TransactionSummary> filterTransactions(String category, LocalDate startDate, LocalDate endDate, Double minAmount, Double maxAmount) {
        return transactionRepository.filterTransactions(category, startDate, endDate, minAmount, maxAmount);
    }

    // Get Transactions by Category
    @Transactional(readOnly = true)
    public List<TransactionSummary> getTransactionsByCategory(int userId, CategoryType category) {
        return transactionRepository.findByUser_UserIDAndCategory(userId, category);
    }

    // Check if budget is exceeded
    @Transactional(readOnly = true)
    public boolean isBudgetExceeded(int userId) {
        double totalExpenses = getTotalExpensesForCurrentMonth(userId);
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return totalExpenses > user.getBudgetLimit();
    }

    // Get Total Expenses for Current Month
    @Transactional(readOnly = true)
    public double getTotalExpensesForCurrentMonth(int userId) {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        Double totalExpenses = transactionRepository.getTotalExpensesForCurrentMonth(userId, firstDay, lastDay);
        return totalExpenses != null ? totalExpenses : 0.0;
    }
    
    // Get total debit for a user in a specific month
    @Transactional(readOnly = true)
    public Double getTotalDebitForMonth(int userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.getTotalAmountByType(userId, startDate, endDate, TransactionType.DEBIT);
    }

    // Get total credit for a user in a specific month
    @Transactional(readOnly = true)
    public Double getTotalCreditForMonth(int userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.getTotalAmountByType(userId, startDate, endDate, TransactionType.CREDIT);
    }
    
    // Get total debit for a user in a specific month and category
    @Transactional(readOnly = true)
    public Double getTotalDebitForCategory(int userId, CategoryType category, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return transactionRepository.getTotalAmountByTypeAndCategory(userId, startDate, endDate, TransactionType.DEBIT, category);
    }
    
    // Get transactions for a user within a date range
    @Transactional(readOnly = true)
    public List<TransactionSummary> getTransactionsByDateRange(int userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findTransactionsByDateRange(userId, startDate, endDate);
    }
}
