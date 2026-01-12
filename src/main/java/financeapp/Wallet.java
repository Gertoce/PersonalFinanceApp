package financeapp;

import java.util.*;

public class Wallet {
    private String owner;
    private double balance;
    private List<Transaction> transactions;
    private Map<String, BudgetCategory> budgetCategories;

    public Wallet(String owner) {
        this.owner = owner;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.budgetCategories = new HashMap<>();
    }

    // Добавление дохода
    public void addIncome(String category, double amount, String description) {
        Transaction transaction = new Transaction("INCOME", category, amount, description);
        transactions.add(transaction);
        balance += amount;
        System.out.println("Доход добавлен: " + category + " - " + amount);
    }

    // Добавление расхода
    public boolean addExpense(String category, double amount, String description) {
        if (balance < amount) {
            System.out.println("Ошибка: Недостаточно средств на балансе!");
            return false;
        }

        Transaction transaction = new Transaction("EXPENSE", category, amount, description);
        transactions.add(transaction);
        balance -= amount;

        // Обновляем траты по категории бюджета
        if (budgetCategories.containsKey(category)) {
            budgetCategories.get(category).addExpense(amount);
        }

        System.out.println("Расход добавлен: " + category + " - " + amount);
        return true;
    }

    // Установка бюджета на категорию
    public void setBudget(String category, double limit) {
        if (budgetCategories.containsKey(category)) {
            budgetCategories.get(category).setBudgetLimit(limit);
        } else {
            budgetCategories.put(category, new BudgetCategory(category, limit));
        }
        System.out.println("Бюджет установлен для категории '" + category + "': " + limit);
    }

    // Получение статистики
    public void printStatistics() {
        System.out.println("\n=== ФИНАНСОВАЯ СТАТИСТИКА ===");
        System.out.println("Владелец: " + owner);
        System.out.println("Текущий баланс: " + String.format("%.2f", balance));

        // Расчет доходов
        double totalIncome = transactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount)
                .sum();
        System.out.println("Общий доход: " + String.format("%.2f", totalIncome));

        // Расчет расходов
        double totalExpense = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount)
                .sum();
        System.out.println("Общие расходы: " + String.format("%.2f", totalExpense));

        // Доходы по категориям
        System.out.println("\nДоходы по категориям:");
        Map<String, Double> incomeByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("INCOME")) {
                incomeByCategory.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }
        incomeByCategory.forEach((cat, amt) ->
                System.out.println("  " + cat + ": " + String.format("%.2f", amt)));

        // Расходы по категориям
        System.out.println("\nРасходы по категориям:");
        Map<String, Double> expenseByCategory = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getType().equals("EXPENSE")) {
                expenseByCategory.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }
        expenseByCategory.forEach((cat, amt) ->
                System.out.println("  " + cat + ": " + String.format("%.2f", amt)));

        // Бюджеты по категориям
        System.out.println("\nБюджет по категориям:");
        budgetCategories.values().forEach(bc -> {
            double spent = expenseByCategory.getOrDefault(bc.getName(), 0.0);
            double remaining = bc.getBudgetLimit() - spent;
            System.out.println(String.format("  %s: Лимит: %.2f, Потрачено: %.2f, Осталось: %.2f",
                    bc.getName(), bc.getBudgetLimit(), spent, remaining));
            if (spent > bc.getBudgetLimit()) {
                System.out.println("    ⚠️  ПРЕВЫШЕН БЮДЖЕТ!");
            }
        });

        // Проверка предупреждений
        checkAlerts();
    }

    // Проверка предупреждений
    private void checkAlerts() {
        // Проверка превышения бюджета
        budgetCategories.values().forEach(bc -> {
            if (bc.isExceeded()) {
                System.out.println("\n⚠️  ВНИМАНИЕ: Превышен бюджет для категории '" + bc.getName() + "'!");
            }
        });

        // Проверка, если расходы превышают доходы
        double totalIncome = getTotalIncome();
        double totalExpense = getTotalExpense();
        if (totalExpense > totalIncome) {
            System.out.println("\n⚠️  ВНИМАНИЕ: Расходы превышают доходы!");
        }
    }

    // Геттеры
    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }
    public Map<String, BudgetCategory> getBudgetCategories() { return budgetCategories; }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    // Перевод другому пользователю
    public boolean transferTo(Wallet recipient, double amount, String description) {
        if (balance < amount) {
            System.out.println("Ошибка: Недостаточно средств для перевода!");
            return false;
        }

        // Создаем расход у отправителя
        Transaction outgoing = new Transaction("EXPENSE", "ПЕРЕВОД", amount,
                "Перевод пользователю: " + recipient.getOwner());
        transactions.add(outgoing);
        balance -= amount;

        // Создаем доход у получателя
        Transaction incoming = new Transaction("INCOME", "ПЕРЕВОД", amount,
                "Перевод от пользователя: " + owner);
        recipient.getTransactions().add(incoming);
        recipient.balance += amount;

        System.out.println(String.format("Перевод выполнен: %.2f от %s к %s",
                amount, owner, recipient.getOwner()));
        return true;
    }
}
