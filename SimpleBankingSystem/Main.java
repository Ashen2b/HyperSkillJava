package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static boolean isLogged = false;
    private static boolean isAlive = true;
    private static int command;

    private static final String JDBC_DRIVER = "jdbc:sqlite:";
    private static String databaseFile;
    private static Connection connection;
    private static Statement statement;

    private static Card currentCard;

    public static void main(String[] args) {
        setDatabaseFileName(args);
        try (Scanner scanner = new Scanner(System.in)) {
            connection = DriverManager.getConnection(JDBC_DRIVER + databaseFile);
            statement = connection.createStatement();
            createTableIfNotExists();
            while (isAlive) {
                displayMenu();
                setCommand(scanner);
                invokeCommand(scanner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                currentCard = null;
                connection.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setDatabaseFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equals(args[i])) {
                databaseFile = args[i + 1];
                break;
            }
        }
    }

    private static void createTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0);";
        statement.executeUpdate(createTableSQL);
    }

    private static void displayMenu() {
        if (isLogged) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
        } else {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
        }
    }

    private static void setCommand(Scanner scanner) {
        try {
            command = Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            System.out.println("Invalid command");
            command = -1;
        }
    }

    private static void invokeCommand(Scanner scanner) {
        if (command == 0) {
            isAlive = false;
            System.out.println("Bye!");
        } else if (isLogged) {
            switch (command) {
                case 1:
                    showBalance();
                    break;
                case 2:
                    addIncome(scanner);
                    break;
                case 3:
                    doTransfer(scanner);
                    break;
                case 4:
                    closeAccount();
                    break;
                case 5:
                    logOut();
                    System.out.println("You have successfully logged out!");
            }
        } else {
            if (command == 1) {
                createAccount();
            } else if (command == 2) {
                logIntoAccount(scanner);
            }
        }
    }

    private static void createAccount() {
        new Card(statement);
    }

    private static void logIntoAccount(Scanner scanner) {
        ArrayList<Card> allCards;
        try {

            System.out.println("Enter your card number:");
            String userCardNum = scanner.next();
            System.out.println("Enter your PIN:");
            String userCardPIN = scanner.next();
            String[] userCardChars = userCardNum.split("");
            int targetCardChecksum = Integer.parseInt(userCardChars[15]);

            if (Card.calculateChecksum(userCardChars) == targetCardChecksum) {
                allCards = getAllCards();
                for (Card card : allCards) {
                    if (card.getCARD_NUM().equals(userCardNum) && card.getPIN().equals(userCardPIN)) {
                        currentCard = card;
                        card.setStatement(statement);
                        isLogged = true;
                        System.out.println("You have successfully logged in!");
                        break;
                    }
                }
            } else if (!isLogged) {
                System.out.println("Wrong card number or PIN!");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException boundsException) {
            System.out.println("Wrong card format!");
        }
    }

    private static void showBalance() {
        System.out.println("Balance: " + currentCard.getBalance());
    }

    private static void addIncome(Scanner scanner) {
        System.out.println("Enter income:");
        try {
            currentCard.incomeValidation(scanner.nextInt());
        } catch (InputMismatchException e) {
            System.out.println("Wrong input format!");
        }
    }

    private static void doTransfer(Scanner scanner) {
        int currentBalance = currentCard.getBalance();
        Savepoint transferPoint = null;
        try {
            transferPoint = connection.setSavepoint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Transfer");
            System.out.println("Enter card number:");
            String targetCard = scanner.next();
            String[] cardChars = targetCard.split("");
            int targetCardChecksum = Integer.parseInt(cardChars[15]);
            if (currentCard.getCARD_NUM().equals(targetCard)) {
                System.out.println("You can't transfer money to the same account!");
            } else if (Card.calculateChecksum(cardChars) != targetCardChecksum) {
                System.out.println("Probably you made a mistake in the card number. Please try again!");
            } else if (!checkIfExists(targetCard)) {
                System.out.println("Such a card does not exist.");
            } else {
                System.out.println("Enter how much money you want to transfer:");
                int transferValue = scanner.nextInt();
                if (transferValue < currentBalance) {
                    currentCard.makeTransaction(targetCard, transferValue);
                } else {
                    System.out.println("Not enough money!");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong card format!");
        } catch (InputMismatchException e) {
            System.out.println("Wrong sum input format!");
        } catch (SQLException exception) {
            System.out.println("Something went wrong!");
            try {
                connection.rollback(transferPoint);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeAccount() {
        currentCard.closeCard();
        logOut();
    }

    private static boolean checkIfExists(String targetCard) {
        ArrayList<Card> allCards = getAllCards();
        for (Card card : allCards) {
            if (card.getCARD_NUM().equals(targetCard)) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        String query = "SELECT id, number, pin FROM card;";
        try {
            ResultSet allCards = statement.executeQuery(query);
            while (allCards.next()) {
                int id = allCards.getInt(1);
                String CARD_NUMBER = allCards.getString(2);
                String PIN = allCards.getString(3);
                cards.add(new Card(statement, id, CARD_NUMBER, PIN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    private static void logOut() {
        currentCard = null;
        isLogged = false;
    }
}
