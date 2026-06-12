import java.util.Scanner;

// ---------- ВИКЛЮЧЕННЯ ----------
class UserLimitException extends Exception {
    public UserLimitException(String message) {
        super(message);
    }
}

class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}

public class AuthenticationSystem {

    static final int MAX_USERS = 15;

    static String[] usernames = new String[MAX_USERS];
    static String[] passwords = new String[MAX_USERS];

    static String[] forbiddenWords = {
            "admin", "pass", "password", "qwerty", "ytrewq"
    };

    static int forbiddenCount = 5;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            try {
                System.out.println("\n===== Меню =====");
                System.out.println("1 - Додати юзера");
                System.out.println("2 - Видалити юзера");
                System.out.println("3 - Авторизуватись");
                System.out.println("4 - Додати заборонено слово");
                System.out.println("5 - Вийти з програми");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        registerUser();
                        break;

                    case 2:
                        deleteUser();
                        break;

                    case 3:
                        authenticateUser();
                        break;

                    case 4:
                        addForbiddenWord();
                        break;

                    case 5:
                        System.out.println("Програма завершена.");
                        return;

                    default:
                        System.out.println("Не існуюче меню.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Введіть число:");
            }
        }
    }

    static void registerUser() {
        try {
            int index = findFreePosition();

            if (index == -1) {
                throw new UserLimitException("Максимальний розмір юзерів.");
            }

            System.out.print("Username: ");
            String username = scanner.nextLine();
            validateUsername(username);

            System.out.print("Password: ");
            String password = scanner.nextLine();
            validatePassword(password);

            usernames[index] = username;
            passwords[index] = password;

            System.out.println("Юзер успішно добавлений.");

        } catch (UserLimitException | InvalidUsernameException | InvalidPasswordException e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteUser() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            int index = findUser(username);

            if (index == -1) {
                throw new UserNotFoundException("Юзер не існує.");
            }

            usernames[index] = null;
            passwords[index] = null;

            System.out.println("Юзер видален.");

        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    static void authenticateUser() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            int index = findUser(username);

            if (index == -1 || !passwords[index].equals(password)) {
                throw new AuthenticationException("Неправильне ім'я юзера або пароль.");
            }

            System.out.println("Юзер авторизован.");

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }

    static void addForbiddenWord() {
        String[] temp = new String[forbiddenCount + 1];

        for (int i = 0; i < forbiddenCount; i++) {
            temp[i] = forbiddenWords[i];
        }

        System.out.print("Введіть нове заборонене слово: ");
        temp[forbiddenCount] = scanner.nextLine();

        forbiddenWords = temp;
        forbiddenCount++;

        System.out.println("Заборонене слово додане!");
    }

    static int findFreePosition() {
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] == null) {
                return i;
            }
        }
        return -1;
    }

    static int findUser(String username) {
        for (int i = 0; i < MAX_USERS; i++) {
            if (usernames[i] != null && usernames[i].equals(username)) {
                return i;
            }
        }
        return -1;
    }

    static void validateUsername(String username) throws InvalidUsernameException {
        if (username.length() < 5) {
            throw new InvalidUsernameException("Ім'я юзера повинно міститись не меньш ніж 5 символів");
        }

        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                throw new InvalidUsernameException("Ім'я юзера не може бути з пробілами");
            }
        }
    }

    static void validatePassword(String password) throws InvalidPasswordException {
        if (password.length() < 10) {
            throw new InvalidPasswordException("Пароль повинен містити не меньш 10 сиволів");
        }

        int digits = 0;
        int special = 0;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (ch == ' ') {
                throw new InvalidPasswordException("Пароль не може бути з пробілами");
            }

            if (Character.isDigit(ch)) {
                digits++;
            } else if (!Character.isLetter(ch)) {
                special++;
            }
        }

        if (digits < 3) {
            throw new InvalidPasswordException("Пароль повинен містити 3 цифри як мінімум");
        }

        if (special < 1) {
            throw new InvalidPasswordException("Пароль повинен містити як мінімум один спеціальний символ");
        }

        String lower = password.toLowerCase();

        for (int i = 0; i < forbiddenCount; i++) {
            if (lower.contains(forbiddenWords[i])) {
                throw new InvalidPasswordException("Пароль містить у собі заборонені слова: " + forbiddenWords[i]);
            }
        }
    }
}