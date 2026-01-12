package financeapp;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String type; // "INCOME" или "EXPENSE"
    private String category;
    private double amount;
    private LocalDateTime dateTime;
    private String description;

    public Transaction(String type, String category, double amount, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.description = description;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s: %.2f | %s",
                dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                type, category, amount, description);
    }
}
