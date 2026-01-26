package com.expensetracker.dao;

import com.expensetracker.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
@Repository
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SessionFactory sessionFactory;

    // Register User
    @Transactional
    public User registerUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error registering user: {}", e.getMessage());
            throw e;
        }
    }

    // Update User
    @Transactional
    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }

    // Delete User
    @Transactional
    public void deleteUser(int userId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
    }

    // Get All Users
    @Transactional
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        }
    }

    // Get User by ID
    @Transactional(readOnly = true)
    public User getUserById(int userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, userId);
    }



    // Update Budget Limit
    @Transactional
    public void updateBudgetLimit(int userId, Double newBudgetLimit, boolean enableAlert) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
            user.setBudgetLimit(newBudgetLimit);
            user.setBudgetAlertEnabled(enableAlert);
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error updating budget limit: {}", e.getMessage());
            throw e;
        }
    }

    // Validate Login
    public boolean isValidUser(String username, String password) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("FROM User WHERE username = :username", User.class)
                               .setParameter("username", username)
                               .uniqueResult();
            return user != null && passwordEncoder.matches(password, user.getPassword());
        }
    }
}
