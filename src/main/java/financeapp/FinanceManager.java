package financeapp;

import java.util.*;

public class FinanceManager {
    private Map<String, User> users;
    private User currentUser;

    public FinanceManager() {
        this.users = new HashMap<>();
        this.currentUser = null;
    }

    // Регистрация пользователя
    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Пользователь с таким логином уже существует!");
            return false;
        }

        User newUser = new User(username, password);
        users.put(username, newUser);
        System.out.println("Пользователь " + username + " успешно зарегистрирован!");
        return true;
    }

    // Авторизация пользователя
    public boolean login(String username, String password) {
        if (!users.containsKey(username)) {
            System.out.println("Пользователь не найден!");
            return false;
        }

        User user = users.get(username);
        if (user.authenticate(password)) {
            currentUser = user;
            System.out.println("Добро пожаловать, " + username + "!");
            return true;
        } else {
            System.out.println("Неверный пароль!");
            return false;
        }
    }

    // Выход из системы
    public void logout() {
        if (currentUser != null) {
            System.out.println("До свидания, " + currentUser.getUsername() + "!");
            currentUser = null;
        }
    }

    // Получение текущего пользователя
    public User getCurrentUser() {
        return currentUser;
    }

    // Получение пользователя по имени (для переводов)
    public User getUserByUsername(String username) {
        return users.get(username);
    }

    // Получение всех пользователей
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
