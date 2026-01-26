package com.expensetracker.entity;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userID;
	private String username;
	private String password;
	private String email;

	private double monthlyBudget;

	private Double budgetLimit;
	private boolean budgetAlertEnabled;

	private Double newBudget;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore // Prevents Jackson from serializing this field
	private List<TransactionSummary> transactions;

	public User() {
		super();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getMonthlyBudget() {
		return monthlyBudget;
	}

	public void setMonthlyBudget(double monthlyBudget) {
		this.monthlyBudget = monthlyBudget;
	}

	public Double getBudgetLimit() {
		return budgetLimit;
	}

	public void setBudgetLimit(Double budgetLimit) {
		this.budgetLimit = budgetLimit;
	}

	public boolean isBudgetAlertEnabled() {
		return budgetAlertEnabled;
	}

	public void setBudgetAlertEnabled(boolean budgetAlertEnabled) {
		this.budgetAlertEnabled = budgetAlertEnabled;
	}

	public Double getNewBudget() {
		return newBudget;
	}

	public void setNewBudget(Double newBudget) {
		this.newBudget = newBudget;
	}

	public List<TransactionSummary> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionSummary> transactions) {
		this.transactions = transactions;
	}

}
