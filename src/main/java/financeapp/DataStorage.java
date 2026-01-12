package financeapp;

import java.io.*;
import java.util.*;

public class DataStorage {
    private static final String DATA_FILE = "finance_data.ser";

    // Сохранение данных
    public static void saveData(FinanceManager financeManager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            Map<String, Object> data = new HashMap<>();

            // Сохраняем пользователей и их кошельки
            Map<String, User> users = new HashMap<>();
            for (User user : financeManager.getAllUsers()) {
                users.put(user.getUsername(), user);
            }

            data.put("users", users);
            data.put("currentUser", financeManager.getCurrentUser() != null ?
                    financeManager.getCurrentUser().getUsername() : null);

            oos.writeObject(data);
            System.out.println("Данные успешно сохранены!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Загрузка данных
    public static FinanceManager loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Файл данных не найден. Создана новая система.");
            return new FinanceManager();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) ois.readObject();

            @SuppressWarnings("unchecked")
            Map<String, User> users = (Map<String, User>) data.get("users");

            FinanceManager financeManager = new FinanceManager();
            for (User user : users.values()) {
                financeManager.getAllUsers().add(user);
            }

            String currentUsername = (String) data.get("currentUser");
            if (currentUsername != null && users.containsKey(currentUsername)) {
                financeManager.login(currentUsername, "");
            }

            System.out.println("Данные успешно загружены!");
            return financeManager;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
            return new FinanceManager();
        }
    }
}
