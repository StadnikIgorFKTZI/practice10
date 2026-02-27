import java.util.Scanner;

public class main {
    private static final int MAX_USERS = 15;
    private static String[] usernames =  new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];

    private static String[] bannedWords = {"admin", "pass", "password", "qwerty", "ytrewq"};

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        while(true) {
            for(int i = 0; i < 50; i++) System.out.println();
            System.out.println("=== СИСТЕМА АУТЕНТИФІКАЦІЇ ===");
             System.out.println("Користувачів: " + countUsers() + "/" + MAX_USERS);
            System.out.println("------------------------------");
            System.out.println("1. Реєстрація");
            System.out.println("2. Видалення за ім'ям");
            System.out.println("3. Вхід (Виконати дію)");
            System.out.println("4. Список усіх користувачів");
            System.out.println("0. Вихід");
            System.out.println("------------------------------");
            System.out.print("Ваш вибір: ");

            try {
                String choice = sc.nextLine();
                if (choice.equals("1")) register();
                else if (choice.equals("2")) remove();
                else if (choice.equals("3")) login();
                else if (choice.equals("4")) showAllUsers();
                else if (choice.equals("0")) break;
                else System.out.println("Невірний пункт.");

                if (!choice.equals("0")) {
                    System.out.println("\nНатисніть Enter, щоб повернутися в меню...");
                    sc.nextLine();
                }
            } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException e) {
                System.out.println("\n[ПОМИЛКА]: " + e.getMessage());
                System.out.println("Натисніть Enter, щоб продовжити...");
                sc.nextLine();
            } catch (SecurityException e) {
                System.out.println("\n[ДОСТУП ЗАБОРОНЕНО]: " + e.getMessage());
                System.out.println("Натисніть Enter, щоб продовжити...");
                sc.nextLine();
            }
        }
    }

    public static void register() {
        int Index = -1;
        for (int i = 0; i < MAX_USERS; i++) {
            if(usernames[i] == null){
                Index = i;
                break;
            }
        }

        if(Index == -1){
            throw new IndexOutOfBoundsException("Більше користувачів не можна додати (макс. 15).");
        }

        System.out.print("Ім'я: ");
        String name = sc.nextLine();
        if (isUsernameTaken(name)) {
            throw new IllegalStateException("Користувач з таким ім'ям вже існує!");
        }
        if (name.length() < 5 || name.contains(" ")) throw new IllegalArgumentException("Ім'я містить помилки (мін. 5 симв., без пробілів).");

        System.out.print("Пароль: ");
        String pass = sc.nextLine();
        validatePassword(pass);

        usernames[Index] = name;
        passwords[Index] = pass;
        System.out.println("Готово.");
    }
    public static void remove() {
        System.out.print("Кого видалити? ");
        String name = sc.nextLine();
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equals(name)) {
                usernames[i] = null;
                passwords[i] = null;
                System.out.println("Користувача видалено.");
                return;
            }
        }
        throw new IllegalArgumentException("Користувача не знайдено.");
    }
    public static void login() {
        System.out.print("Логін: ");
        String username = sc.nextLine();
        System.out.print("Пароль: ");
        String pass = sc.nextLine();

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equals(username)) {
                if (passwords[i].equals(pass)) {
                    System.out.println("АУТЕНТИФІКОВАНО! Дія дозволена.");
                    return;
                }
                throw new SecurityException("Невірний пароль.");
            }
        }
        throw new IllegalArgumentException("Користувача не існує.");
    }

    private static void validatePassword(String pass) {
        if (pass.length() < 10) throw new IllegalArgumentException("Пароль має бути від 10 символів.");
        if (pass.contains(" ")) throw new IllegalArgumentException("Пароль не може містити пробіли.");

        int digitCount = 0;
        boolean hasSpecial = false;
        String specialChars = "!@#$%^&*()-_=+[]{};:'\",.<>/?|\\";

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);

            if (Character.isDigit(c)) digitCount++;

            boolean isLatin = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
            boolean isDigit = (c >= '0' && c <= '9');
            boolean isSpecial = false;
            for (int j = 0; j < specialChars.length(); j++) {
                if (c == specialChars.charAt(j)) {
                    isSpecial = true;
                    hasSpecial = true;
                    break;
                }
            }

            if (!isLatin && !isDigit && !isSpecial) {
                throw new IllegalArgumentException("Пароль містить недозволені символи (тільки латиниця, цифри та спецсимволи).");
            }
        }

        if (digitCount < 3) throw new IllegalArgumentException("Пароль має містити мінімум 3 цифри.");
        if (!hasSpecial) throw new IllegalArgumentException("Пароль має містити хоча б 1 спецсимвол.");

        for (String banned : bannedWords) {
            if (pass.toLowerCase().contains(banned.toLowerCase())) {
                throw new IllegalArgumentException("Пароль містить заборонену комбінацію: " + banned);
            }
        }

    }

    private static int countUsers() {
        int count = 0;
        for (String u : usernames) if (u != null) count++;
        return count;
    }

    private static void showAllUsers() {
        System.out.println("\n--- СПИСОК КОРИСТУВАЧІВ ---");
        boolean isEmpty = true;
        int counter = 1;

        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null) {
                System.out.println(counter + ". " + usernames[i]);
                counter++;
                isEmpty = false;
            }
        }

        if (isEmpty) {
            System.out.println("Список порожній.");
        }
        System.out.println("---------------------------");
    }

    private static boolean isUsernameTaken(String name) {
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
