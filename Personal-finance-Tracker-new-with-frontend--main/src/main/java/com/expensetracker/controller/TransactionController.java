package com.expensetracker.controller;

import com.expensetracker.entity.TransactionSummary;
import com.expensetracker.entity.CategoryType;
import com.expensetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	// Add a new transaction for a user
	@PostMapping("/add/{userId}")
	public ResponseEntity<String> addTransaction(@PathVariable int userId,
			@RequestBody TransactionSummary transaction) {
		transactionService.addTransaction(userId, transaction);
		return ResponseEntity.ok("Transaction added successfully");
	}

	// Get all transactions for a user
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TransactionSummary>> getTransactionsByUser(@PathVariable int userId) {
		return ResponseEntity.ok(transactionService.getAllTransactions(userId));
	}

	// Update an existing transaction
	@PutMapping("/{transactionId}")
	public ResponseEntity<String> updateTransaction(@PathVariable int transactionId,
			@RequestBody TransactionSummary transaction) {
		transaction.setTraID(transactionId);
		transactionService.updateTransaction(transaction);
		return ResponseEntity.ok("Transaction updated successfully");
	}

	// Delete a transaction by ID
	@DeleteMapping("/{transactionId}")
	public ResponseEntity<String> deleteTransaction(@PathVariable int transactionId) {
		transactionService.deleteTransaction(transactionId);
		return ResponseEntity.ok("Transaction deleted successfully");
	}

	// Search & Filter Transactions
	@GetMapping("/filter")
	public ResponseEntity<List<TransactionSummary>> filterTransactions(@RequestParam(required = false) String category,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Double minAmount, @RequestParam(required = false) Double maxAmount) {

		return ResponseEntity
				.ok(transactionService.filterTransactions(category, startDate, endDate, minAmount, maxAmount));
	}

	// Get transactions by category for a user
	@GetMapping("/user/{userId}/category/{category}")
	public ResponseEntity<List<TransactionSummary>> getTransactionsByCategory(@PathVariable int userId,
			@PathVariable String category) {
		try {
			CategoryType categoryType = CategoryType.fromString(category); // Convert string to enum
			return ResponseEntity.ok(transactionService.getTransactionsByCategory(userId, categoryType));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null); // Return 400 Bad Request for invalid category
		}
	}

	// Check if budget is exceeded for a user
	@GetMapping("/budget-status/{userId}")
	public ResponseEntity<Object> checkBudgetStatus(@PathVariable int userId) {
		boolean exceeded = transactionService.isBudgetExceeded(userId);
		return ResponseEntity.ok().body(exceeded ? new BudgetResponse("Budget exceeded!", true)
				: new BudgetResponse("You are within your budget.", false));
	}

	// Inner class for structured JSON response
	static class BudgetResponse {
		private String message;
		private boolean exceeded;

		public BudgetResponse(String message, boolean exceeded) {
			this.message = message;
			this.exceeded = exceeded;
		}

		public String getMessage() {
			return message;
		}

		public boolean isExceeded() {
			return exceeded;
		}
	}

	// Get total debit for a user in a specific month
	@GetMapping("/{userId}/debit/{month}/{year}")
	public ResponseEntity<Double> getTotalDebitForMonth(@PathVariable int userId, @PathVariable int month,
			@PathVariable int year) {
		Double totalDebit = transactionService.getTotalDebitForMonth(userId, month, year);
		return ResponseEntity.ok(totalDebit != null ? totalDebit : 0.0);
	}

	// Get total credit for a user in a specific month
	@GetMapping("/{userId}/credit/{month}/{year}")
	public ResponseEntity<Double> getTotalCreditForMonth(@PathVariable int userId, @PathVariable int month,
			@PathVariable int year) {
		Double totalCredit = transactionService.getTotalCreditForMonth(userId, month, year);
		return ResponseEntity.ok(totalCredit != null ? totalCredit : 0.0);
	}

	// Get total debit for a user in a specific month and category
	@GetMapping("/{userId}/debit/{category}/{month}/{year}")
	public ResponseEntity<Double> getTotalDebitForCategory(@PathVariable int userId,
			@PathVariable CategoryType category, @PathVariable int month, @PathVariable int year) {
		Double totalDebit = transactionService.getTotalDebitForCategory(userId, category, month, year);
		return ResponseEntity.ok(totalDebit != null ? totalDebit : 0.0);
	}

	// Get transactions for a user within a date range
	@GetMapping("/{userId}/date-range")
	public ResponseEntity<List<TransactionSummary>> getTransactionsByDateRange(@PathVariable int userId,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		List<TransactionSummary> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
		return ResponseEntity.ok(transactions);
	}
}
