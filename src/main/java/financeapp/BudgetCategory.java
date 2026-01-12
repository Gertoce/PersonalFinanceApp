package financeapp;

public class BudgetCategory {
    private String name;
    private double budgetLimit;
    private double currentSpending;

    public BudgetCategory(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.currentSpending = 0.0;
    }

    public void addExpense(double amount) {
        this.currentSpending += amount;
    }

    public double getRemainingBudget() {
        return budgetLimit - currentSpending;
    }

    public boolean isExceeded() {
        return currentSpending > budgetLimit;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public double getBudgetLimit() { return budgetLimit; }
    public double getCurrentSpending() { return currentSpending; }
    public void setBudgetLimit(double budgetLimit) { this.budgetLimit = budgetLimit; }
    public void resetSpending() { this.currentSpending = 0.0; }

    @Override
    public String toString() {
        return String.format("%s: Лимит: %.2f, Потрачено: %.2f, Осталось: %.2f",
                name, budgetLimit, currentSpending, getRemainingBudget());
    }
}