package com.expensetracker.Repository;

import com.expensetracker.entity.CategoryType;
import com.expensetracker.entity.TransactionSummary;
import com.expensetracker.entity.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionSummary, Integer> {

    List<TransactionSummary> findByUser_UserID(int userId);

    List<TransactionSummary> findByUser_UserIDAndCategory(int userId, CategoryType category);

    @Query("SELECT SUM(t.amount) FROM TransactionSummary t WHERE t.user.userID = :userId AND t.type = 'DEBIT'")
    Double getTotalSpentByUser(@Param("userId") int userId);

    @Query("SELECT SUM(t.amount) FROM TransactionSummary t WHERE t.user.userID = :userId AND t.transactionDate BETWEEN :firstDay AND :lastDay AND t.type = 'DEBIT'")
    Double getTotalExpensesForCurrentMonth(@Param("userId") int userId, @Param("firstDay") LocalDate firstDay, @Param("lastDay") LocalDate lastDay);

    @Query("SELECT t FROM TransactionSummary t WHERE (:category IS NULL OR t.category = :category) AND " +
           "(:startDate IS NULL OR t.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.transactionDate <= :endDate) AND " +
           "(:minAmount IS NULL OR t.amount >= :minAmount) AND " +
           "(:maxAmount IS NULL OR t.amount <= :maxAmount)")
    List<TransactionSummary> filterTransactions(@Param("category") String category,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("minAmount") Double minAmount,
                                                @Param("maxAmount") Double maxAmount);
    

    @Query("SELECT SUM(t.amount) FROM TransactionSummary t WHERE t.user.userID = :userId AND t.transactionDate BETWEEN :startDate AND :endDate AND t.type = :transactionType")
    Double getTotalAmountByType(@Param("userId") int userId,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("transactionType") TransactionType transactionType);
    

    @Query("SELECT SUM(t.amount) FROM TransactionSummary t WHERE t.user.userID = :userId AND t.transactionDate BETWEEN :startDate AND :endDate AND t.type = :transactionType AND t.category = :category")
    Double getTotalAmountByTypeAndCategory(
            @Param("userId") int userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("transactionType") TransactionType transactionType,
            @Param("category") CategoryType category);
    
    @Query("SELECT t FROM TransactionSummary t WHERE t.user.userID = :userId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionSummary> findTransactionsByDateRange(
            @Param("userId") int userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

