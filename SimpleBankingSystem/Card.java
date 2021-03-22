package banking;

import java.sql.*;
import java.util.Random;

class Card {
    private Statement statement;

    private final String BIN = "400000";
    private final String accountIdentifier;
    private final String checksum;
    private final String CARD_NUM;
    private final String PIN;

    private final int CARD_ID;

    public Card(Statement statement) {
        this.statement = statement;

        this.accountIdentifier = generateIdentifier();
        this.checksum = String.valueOf(generateChecksum());
        this.CARD_NUM = generateCardNum();
        this.PIN = generatePIN();
        this.CARD_ID = countCARD_ID();

        // Just in theory it can generate same card number twice
        // It's a good idea to double check if there's already such a card.
        saveCard();

        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + CARD_NUM);
        System.out.println("Your card PIN:\n" + PIN);
    }

    public Card(Statement statement, int CARD_ID, String CARD_NUM, String PIN) {
        this.statement = statement;
        this.CARD_ID = CARD_ID;

        this.accountIdentifier = CARD_NUM.substring(6, 14);
        this.checksum = CARD_NUM.substring(15);
        this.CARD_NUM = CARD_NUM;

        this.PIN = PIN;
    }

    public String getCARD_NUM() {
        return this.CARD_NUM;
    }

    public String getPIN() {
        return this.PIN;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public int getBalance() {
        int balance = 0;
        try {
            ResultSet result = statement.executeQuery(String.format("SELECT balance FROM card WHERE id=%d", CARD_ID));
            balance = result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    private int countCARD_ID() {
        int id = 0;
        try {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM card;");
            while (result.next()) {
                id = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    private String generateIdentifier() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    private int generateChecksum() {
        return calculateChecksum((this.BIN + this.accountIdentifier).split(""));
    }

    public static int calculateChecksum(String[] digits) {
        int numSum = 0;
        int checkSum;
        int digit;
        for (int i = 0; i < 15; i++) {
            digit = i % 2 == 0 ? Integer.parseInt(digits[i]) * 2 : Integer.parseInt(digits[i]);
            if (digit > 9) {
                digit -= 9;
            }
            numSum += digit;
        }
        checkSum = numSum % 10 == 0 ? 0 : ((numSum / 10 + 1) * 10) - numSum;
        return checkSum;
    }

    private String generateCardNum() {
        return String.format("%s%s%s", BIN, accountIdentifier, checksum);
    }

    private String generatePIN() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public void incomeValidation(int income) {
        String incomeQuery = String.format("UPDATE card SET balance=%d+balance WHERE id=%d;", income, CARD_ID);
        try {
            statement.executeUpdate(incomeQuery);
            System.out.println("Income was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeCard() {
        String closeQuery = String.format("DELETE FROM card WHERE number='%s';", CARD_NUM);
        try {
            statement.executeUpdate(closeQuery);
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeTransaction(String targetCard, int transferValue) throws SQLException {
        String ownerCardQuery = String.format("UPDATE card SET balance=balance - %d" +
                " WHERE number='%s';", transferValue, CARD_NUM);
        String targetCardQuery = String.format("UPDATE card SET balance=balance + %d" +
                " WHERE number='%s';", transferValue, targetCard);
        Connection connection = statement.getConnection();
        connection.setAutoCommit(false);
        try {
            statement.executeUpdate(ownerCardQuery);
            statement.executeUpdate(targetCardQuery);
            connection.commit();
            System.out.println("Success!");
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void saveCard() {
        String SQL = String.format("INSERT INTO card VALUES (%d, %s, %s, 0);", CARD_ID, CARD_NUM, PIN);
        try {
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
