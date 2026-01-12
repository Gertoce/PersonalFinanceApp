package financeapp;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(username);
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Wallet getWallet() { return wallet; }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User: " + username + " | Wallet balance: " + wallet.getBalance();
    }
}
