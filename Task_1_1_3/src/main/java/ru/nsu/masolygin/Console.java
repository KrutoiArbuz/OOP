package ru.nsu.masolygin;

import java.util.Scanner;

/**
 * Консоль для ввода и вывода.
 */
public class Console {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Создает новую консоль.
     */
    public Console() {
    }

    /**
     * Читает строку из консоли.
     *
     * @return введенная строка
     */
    public String readLine() {
        System.out.print("Enter expression: ");
        return scanner.nextLine();
    }

    /**
     * Выводит сообщение об ошибке.
     *
     * @param message текст ошибки
     */
    public void printError(String message) {
        System.err.println("Error: " + message);
    }

    /**
     * Выводит информационное сообщение.
     *
     * @param message текст сообщения
     */
    public void printInfo(String message) {
        System.out.println(message);
    }

    /**
     * Выводит приветствие.
     */
    public void printWelcome() {
        System.out.println("Mathematical Expression Calculator");
        System.out.println("Enter 'exit' to quit");
    }

    /**
     * Выводит прощание.
     */
    public void printGoodbye() {
        System.out.println("Calculator closed.");
    }

    /**
     * Выводит метку выражения.
     *
     * @param label текст метки
     */
    public void printExpression(String label) {
        System.out.print(label + ": ");
    }

    /**
     * Выводит новую строку.
     */
    public void printNewLine() {
        System.out.println();
    }

    /**
     * Запрашивает значение переменной.
     *
     * @param variableName имя переменной
     * @return значение переменной
     */
    public int requestVariableValue(String variableName) {
        while (true) {
            try {
                System.out.print("Enter value for variable '" + variableName + "': ");
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Запрашивает переменную для производной.
     *
     * @return имя переменной
     */
    public String requestDerivativeVariable() {
        System.out.print("Calculate derivative? Enter variable name (or press Enter to skip): ");
        return scanner.nextLine().trim();
    }

    /**
     * Подтверждает вычисление производной.
     *
     * @return true если нужно вычислить
     */
    public boolean confirmDerivativeEvaluation() {
        System.out.print("Evaluate derivative? (y/n): ");
        String choice = scanner.nextLine().trim();
        return "y".equalsIgnoreCase(choice);
    }

    /**
     * Проверяет команду выхода.
     *
     * @param input входная строка
     * @return true если команда выхода
     */
    public boolean isExitCommand(String input) {
        return "exit".equalsIgnoreCase(input.trim());
    }

    /**
     * Проверяет ошибки в строке.
     *
     * @param input входная строка
     * @return true если есть ошибки
     */
    public boolean hasErrors(String input) {

        if (!input.matches("[a-zA-Z0-9+\\-*/^(). ]+")) {
            return true;
        }

        int parenCount = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                parenCount++;
            } else if (c == ')') {
                parenCount--;
            }
            if (parenCount < 0) {
                return true;
            }
        }

        if (parenCount != 0) {
            return true;
        }

        if (input.contains("()")) {
            return true;
        }

        if (input.contains("++") || input.contains("--")
                || input.contains("**") || input.contains("//")
                || input.contains("(+") || input.contains("(-)")) {
            return true;
        }

        return false;
    }
}
