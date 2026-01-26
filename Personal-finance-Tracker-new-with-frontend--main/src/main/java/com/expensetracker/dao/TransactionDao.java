package com.expensetracker.dao;

import com.expensetracker.entity.CategoryType;
import com.expensetracker.entity.TransactionSummary;
import com.expensetracker.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional // Ensures Spring manages transactions automatically ✅
public class TransactionDao {

    @Autowired
    private SessionFactory sessionFactory;

    // ✅ Add a transaction with budget validation
    @Transactional
    public void addTransaction(int userId, TransactionSummary transaction) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // ✅ Calculate total expenses
        String hql = "SELECT SUM(amount) FROM TransactionSummary WHERE user.userID = :userId AND type = 'DEBIT'";
        Double totalSpent = (Double) session.createQuery(hql)
                .setParameter("userId", userId)
                .uniqueResult();
        totalSpent = (totalSpent != null) ? totalSpent : 0.0;

        // ✅ Budget Exceeded Check
        if (user.isBudgetAlertEnabled() && totalSpent + transaction.getAmount() > user.getBudgetLimit()) {
            throw new RuntimeException("Budget exceeded! Your spending limit is ₹" + user.getBudgetLimit());
        }

        transaction.setUser(user);
        session.persist(transaction);
    }

    // ✅ Get all transactions for a user
    @Transactional
    public List<TransactionSummary> getAllTransactions(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM TransactionSummary WHERE user.userID = :userId";
        Query<TransactionSummary> query = session.createQuery(hql, TransactionSummary.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // ✅ Update a transaction
    @Transactional
    public void updateTransaction(TransactionSummary transaction) {
        Session session = sessionFactory.getCurrentSession();
        TransactionSummary existingTransaction = session.get(TransactionSummary.class, transaction.getTraID());
        if (existingTransaction == null) {
            throw new IllegalArgumentException("Transaction not found with ID: " + transaction.getTraID());
        }
        
        // ✅ Update fields while keeping the user unchanged
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setType(transaction.getType());
        existingTransaction.setTransactionDate(transaction.getTransactionDate());
        
        session.update(existingTransaction);
    }

    // ✅ Delete a transaction
    @Transactional
    public void deleteTransaction(int transactionId) {
        Session session = sessionFactory.getCurrentSession();
        TransactionSummary transaction = session.get(TransactionSummary.class, transactionId);
        if (transaction != null) {
            session.delete(transaction);
        }
    }

    // ✅ Filter Transactions with Optional Parameters
    @Transactional
    public List<TransactionSummary> filterTransactions(String category, LocalDate startDate, LocalDate endDate, Double minAmount, Double maxAmount) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM TransactionSummary t WHERE 1=1";
        if (category != null && !category.isEmpty()) hql += " AND t.category = :category";
        if (startDate != null) hql += " AND t.transactionDate >= :startDate";
        if (endDate != null) hql += " AND t.transactionDate <= :endDate";
        if (minAmount != null) hql += " AND t.amount >= :minAmount";
        if (maxAmount != null) hql += " AND t.amount <= :maxAmount";

        Query<TransactionSummary> query = session.createQuery(hql, TransactionSummary.class);
        if (category != null && !category.isEmpty()) query.setParameter("category", category);
        if (startDate != null) query.setParameter("startDate", startDate);
        if (endDate != null) query.setParameter("endDate", endDate);
        if (minAmount != null) query.setParameter("minAmount", minAmount);
        if (maxAmount != null) query.setParameter("maxAmount", maxAmount);

        return query.getResultList();
    }

    // ✅ Get Transactions by Category
    @Transactional
    public List<TransactionSummary> getTransactionsByCategory(int userId, CategoryType category) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM TransactionSummary WHERE user.userID = :userId AND category = :category";
            Query<TransactionSummary> query = session.createQuery(hql, TransactionSummary.class);
            query.setParameter("userId", userId);
            query.setParameter("category", category);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching transactions for category: " + category, e);
        }
    }


    // ✅ Get Total Expenses for Current Month (Fixed Date Logic)
    @Transactional
    public double getTotalExpensesForCurrentMonth(int userId) {
        Session session = sessionFactory.getCurrentSession();
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        String hql = "SELECT SUM(t.amount) FROM TransactionSummary t WHERE t.user.userID = :userId " +
                     "AND t.transactionDate BETWEEN :firstDay AND :lastDay " +
                     "AND t.type = 'DEBIT'";

        Query<Double> query = session.createQuery(hql, Double.class);
        query.setParameter("userId", userId);
        query.setParameter("firstDay", firstDay);
        query.setParameter("lastDay", lastDay);

        Double totalExpenses = query.uniqueResult();
        return totalExpenses != null ? totalExpenses : 0.0;
    }
}
