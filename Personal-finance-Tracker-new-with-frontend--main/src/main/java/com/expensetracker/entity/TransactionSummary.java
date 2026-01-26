package com.expensetracker.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TransactionSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int traID;

	private double amount;
//	private String category;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private TransactionType type;

	 @Enumerated(EnumType.STRING)
	 private CategoryType category;

	private LocalDate transactionDate;

	public TransactionSummary() {
		super();
	}

	@ManyToOne
	@JoinColumn(name = "userid", nullable = false)
	@JsonIgnore
	private User user;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	

	public CategoryType getCategory() {
		return category;
	}

	public void setCategory(CategoryType category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public int getTraID() {
		return traID;
	}

	public void setTraID(int traID) {
		this.traID = traID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
