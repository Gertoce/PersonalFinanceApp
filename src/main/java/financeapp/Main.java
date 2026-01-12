package financeapp;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ПРИЛОЖЕНИЕ ДЛЯ УПРАВЛЕНИЯ ЛИЧНЫМИ ФИНАНСАМИ ===");

        // Загрузка данных
        FinanceManager financeManager = DataStorage.loadData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (financeManager.getCurrentUser() == null) {
                // Меню авторизации
                System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
                System.out.println("1. Войти");
                System.out.println("2. Зарегистрироваться");
                System.out.println("3. Выйти из приложения");
                System.out.print("Выберите действие: ");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            System.out.print("Введите логин: ");
                            String loginUsername = scanner.nextLine();
                            System.out.print("Введите пароль: ");
                            String loginPassword = scanner.nextLine();
                            financeManager.login(loginUsername, loginPassword);
                            break;
                        case 2:
                            System.out.print("Введите новый логин: ");
                            String regUsername = scanner.nextLine();
                            System.out.print("Введите пароль: ");
                            String regPassword = scanner.nextLine();
                            financeManager.register(regUsername, regPassword);
                            break;
                        case 3:
                            System.out.println("До свидания!");
                            DataStorage.saveData(financeManager);
                            scanner.close();
                            return;
                        default:
                            System.out.println("Неверный выбор!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите число!");
                }
            } else {
                // Меню пользователя
                User currentUser = financeManager.getCurrentUser();
                Wallet wallet = currentUser.getWallet();

                System.out.println("\n=== ЛИЧНЫЙ КАБИНЕТ ===");
                System.out.println("Пользователь: " + currentUser.getUsername());
                System.out.println("Баланс: " + String.format("%.2f", wallet.getBalance()));
                System.out.println("1. Добавить доход");
                System.out.println("2. Добавить расход");
                System.out.println("3. Установить бюджет");
                System.out.println("4. Показать статистику");
                System.out.println("5. Показать историю операций");
                System.out.println("6. Сделать перевод");
                System.out.println("7. Выйти из аккаунта");
                System.out.println("8. Сохранить и выйти");
                System.out.print("Выберите действие: ");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1:
                            System.out.print("Введите категорию дохода: ");
                            String incomeCategory = scanner.nextLine();
                            System.out.print("Введите сумму: ");
                            double incomeAmount = Double.parseDouble(scanner.nextLine());
                            System.out.print("Введите описание: ");
                            String incomeDescription = scanner.nextLine();
                            wallet.addIncome(incomeCategory, incomeAmount, incomeDescription);
                            break;
                        case 2:
                            System.out.print("Введите категорию расхода: ");
                            String expenseCategory = scanner.nextLine();
                            System.out.print("Введите сумму: ");
                            double expenseAmount = Double.parseDouble(scanner.nextLine());
                            System.out.print("Введите описание: ");
                            String expenseDescription = scanner.nextLine();
                            wallet.addExpense(expenseCategory, expenseAmount, expenseDescription);
                            break;
                        case 3:
                            System.out.print("Введите категорию для бюджета: ");
                            String budgetCategory = scanner.nextLine();
                            System.out.print("Введите лимит бюджета: ");
                            double budgetLimit = Double.parseDouble(scanner.nextLine());
                            wallet.setBudget(budgetCategory, budgetLimit);
                            break;
                        case 4:
                            wallet.printStatistics();
                            break;
                        case 5:
                            System.out.println("\n=== ИСТОРИЯ ОПЕРАЦИЙ ===");
                            for (Transaction t : wallet.getTransactions()) {
                                System.out.println(t);
                            }
                            break;
                        case 6:
                            System.out.print("Введите логин получателя: ");
                            String recipientUsername = scanner.nextLine();
                            User recipient = financeManager.getUserByUsername(recipientUsername);
                            if (recipient == null) {
                                System.out.println("Пользователь не найден!");
                                break;
                            }
                            System.out.print("Введите сумму перевода: ");
                            double transferAmount = Double.parseDouble(scanner.nextLine());
                            System.out.print("Введите описание: ");
                            String transferDescription = scanner.nextLine();
                            wallet.transferTo(recipient.getWallet(), transferAmount, transferDescription);
                            break;
                        case 7:
                            financeManager.logout();
                            break;
                        case 8:
                            DataStorage.saveData(financeManager);
                            System.out.println("Данные сохранены. До свидания!");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Неверный выбор!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число!");
                } catch (Exception e) {
                    System.out.println("Произошла ошибка: " + e.getMessage());
                }
            }
        }
    }
}
