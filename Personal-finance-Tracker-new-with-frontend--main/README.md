# Expense Tracker

This project is an Expense Tracker application built using Angular for the frontend and Spring Boot for the backend. It allows users to track their income and expenses, view reports, and manage their budget.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
  - [Frontend](#frontend)
  - [Backend](#backend)
- [Usage](#usage)
- [Project Architecture](#project-architecture)
- [Contributing](#contributing)
- [License](#license)

## Features

- User registration and authentication
- Dashboard to view account summary, recent transactions, and budget progress
- Add and manage transactions
- View detailed reports and analytics
- Set and track budget goals

## Technologies Used

- **Frontend**: Angular CLI version 16.0.3
- **Backend**: Spring Boot
- **Database**:  MySQL
- **Build Tool**: Maven/Gradle (for backend)

## Setup

### Frontend

1. **Clone the repository**:

   ```bash
   git clone https://github.com/sajal-0/Personal-finance-Tracker-new-with-frontend-
   cd expense-tracker/frontend
   ```

2. **Install dependencies**:

   ```bash
   npm install
   ```

3. **Run the development server**:

   ```bash
   ng serve
   ```

4. **Access the application**:
   Open your browser and navigate to [http://localhost:4200/](http://localhost:4200/).

### Backend

1. **Clone the repository**:

   ```bash
   git clone https://github.com/sajal-0/Personal-finance-Tracker-new-with-frontend-
   cd expense-tracker/backend
   ```

2. **Build the project**:

   ```bash
   ./mvnw clean install  # For Maven
   ```

3. **Run the application**:

   ```bash
   ./mvnw spring-boot:run  # For Maven
   ```

4. **Access the backend**:
   The backend server will be running on [http://localhost:8080/](http://localhost:8080/).

## Usage

### Register/Login:

- Register a new account or log in with existing credentials.

### Dashboard:

- View your account summary, recent transactions, and budget progress.

### Transactions:

- Add new transactions and view your transaction history.

### Reports:

- Generate and view detailed reports and analytics.

### Budget:

- Set and track your budget goals.

## Project Architecture

### 1. Frontend (Angular)

#### Modules:

- **AppModule**: The root module that bootstraps the application.
- **CoreModule**: Contains singleton services like AuthService, guards, and interceptors.
- **SharedModule**: Contains reusable components, directives, pipes, and common services.
- **Feature Modules**: Separate modules for different features like HomeModule, TransactionModule, ReportModule, ProfileModule, BudgetModule.

#### Components:

- **AppComponent**: The root component.
- **HomeComponent**: Displays the dashboard with account summary, recent transactions, and budget progress.
- **LoginComponent**: Handles user login.
- **RegisterComponent**: Handles user registration.
- **TransactionComponent**: Manages adding and viewing transactions.
- **ReportComponent**: Displays reports and analytics.
- **ProfileComponent**: Manages user profile and settings.
- **BudgetComponent**: Manages budget settings and tracking.

#### Services:

- **AuthService**: Handles authentication and user management.
- **TransactionService**: Manages transaction-related operations.
- **ReportService**: Fetches and processes report data.
- **BudgetService**: Manages budget-related operations.

#### Guards:

- **AuthGuard**: Protects routes that require authentication.

#### Interceptors:

- **AuthInterceptor**: Adds authentication tokens to HTTP requests.

### 2. Backend (Spring Boot)

#### Controllers:

- **UserController**: Handles user registration, login, and profile management.
- **TransactionController**: Manages transaction-related operations.
- **ReportController**: Provides endpoints for generating reports.
- **BudgetController**: Manages budget-related operations.
- **AccountController**: Provides account summary data.

#### Services:

- **UserService**: Contains business logic for user management.
- **TransactionService**: Contains business logic for transaction management.
- **ReportService**: Contains business logic for generating reports.
- **BudgetService**: Contains business logic for budget management.
- **AccountService**: Contains business logic for fetching account summary data.

#### Repositories:

- **UserRepository**: Interacts with the user database.
- **TransactionRepository**: Interacts with the transaction database.
- **BudgetRepository**: Interacts with the budget database.

#### Entities:

- **User**: Represents a user in the system.
- **Transaction**: Represents a financial transaction.
- **Budget**: Represents a budget setting.

#### DTOs:

- **UserDTO**: Data transfer object for user data.
- **TransactionDTO**: Data transfer object for transaction data.
- **BudgetDTO**: Data transfer object for budget data.
- **AccountSummaryDTO**: Data transfer object for account summary data.

#### Security:

- **JwtAuthenticationFilter**: Handles JWT authentication.
- **CustomUserDetailsService**: Loads user-specific data.

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**.
2. **Create a new branch**:
   ```bash
   git checkout -b feature-branch
   ```
3. **Commit your changes**:
   ```bash
   git commit -am "Add new feature"
   ```
4. **Push to the branch**:
   ```bash
   git push origin feature-branch
   ```
5. **Create a new Pull Request**.

## Customization

- **Repository URL**: Replace [https://github.com/sajal-0/Personal-finance-Tracker-new-with-frontend-](https://github.com/sajal-0/Personal-finance-Tracker-new-with-frontend-) with the actual URL of your repository.
- **Database**:  MySQL.



