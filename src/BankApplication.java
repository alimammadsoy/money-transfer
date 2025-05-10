import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;
import service.AccountService;
import service.AccountServiceImpl;
import util.ConsoleHelper;

import java.util.List;

public class BankApplication {
    private static final AccountService accountService = new AccountServiceImpl();
    private static Account currentAccount = null;

    public static void main(String[] args) {
        // Create some demo accounts
        setupDemoAccounts();

        while (true) {
            if (currentAccount == null) {
                boolean shouldExit = showLoginMenu();
                if (shouldExit) {
                    System.out.println("Thank you for using our system. Goodbye!");
                    System.exit(0);
                }
            } else {
                displayMainMenu();
                int choice = ConsoleHelper.readIntFromConsole("Enter your choice: ", 1, 5);

                switch (choice) {
                    case 1:
                        showBalance();
                        break;
                    case 2:
                        sendMoney();
                        break;
                    case 3:
                        viewTransactionHistory();
                        break;
                    case 4:
                        logout();
                        break;
                    case 5:
                        System.out.println("Thank you for using our system. Goodbye!");
                        System.exit(0);
                        break;
                }
            }
        }
    }

    private static void setupDemoAccounts() {
        accountService.createAccount("1111222233334444", "Ali Mammadov", 1000.0, "1234");
        accountService.createAccount("5555666677778888", "Sahrab Suleymanov", 2000.0, "5678");
        accountService.createAccount("9999000011112222", "Javid Umudov", 1500.0, "9012");
        accountService.createAccount("3333444455556666", "Farman Hasanli", 3000.0, "3456");
    }

    private static boolean showLoginMenu() {
        ConsoleHelper.printMenuHeader("WELCOME TO THE BANK TRANSFER SYSTEM");
        System.out.println("1. Login");
        System.out.println("2. Exit");

        int choice = ConsoleHelper.readIntFromConsole("Enter your choice: ", 1, 2);

        switch (choice) {
            case 1:
                login();
                return false;
            case 2:
                return true;
            default:
                return false;
        }
    }

    private static void login() {
        ConsoleHelper.printMenuHeader("LOGIN");
        String cardNumber = ConsoleHelper.readStringFromConsole("Enter your card number: ");
        String pin = ConsoleHelper.readStringFromConsole("Enter your PIN: ");

        try {
            boolean isAuthenticated = accountService.authenticateAccount(cardNumber, pin);

            if (isAuthenticated) {
                currentAccount = accountService.getAccountByCardNumber(cardNumber);
                System.out.println("Login successful. Welcome, " + currentAccount.getOwnerName() + "!");
            } else {
                System.out.println("Authentication failed. Invalid PIN.");
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        ConsoleHelper.pause("");
    }

    private static void displayMainMenu() {
        ConsoleHelper.printMenuHeader("MAIN MENU");
        System.out.println("Welcome, " + currentAccount.getOwnerName());
        System.out.println("1. Show My Balance");
        System.out.println("2. Send Money to Another Account");
        System.out.println("3. View Transaction History");
        System.out.println("4. Logout");
        System.out.println("5. Exit");
    }

    private static void showBalance() {
        ConsoleHelper.printMenuHeader("ACCOUNT BALANCE");
        System.out.println("Account Owner: " + currentAccount.getOwnerName());
        System.out.println("Card Number: " + currentAccount.getCardNumber());
        System.out.printf("Current Balance: $%.2f%n", currentAccount.getBalance());

        ConsoleHelper.pause("");
    }

    private static void sendMoney() {
        ConsoleHelper.printMenuHeader("SEND MONEY");

        String destinationCardNumber = ConsoleHelper.readStringFromConsole("Enter destination card number: ");

        if (destinationCardNumber.equals(currentAccount.getCardNumber())) {
            System.out.println("Error: Cannot send money to your own account");
            ConsoleHelper.pause("");
            return;
        }

        double amount = ConsoleHelper.readDoubleFromConsole("Enter amount to send: $", 0.01);
        String pin = ConsoleHelper.readStringFromConsole("Enter your PIN to confirm: ");

        try {
            accountService.transferMoney(currentAccount.getCardNumber(), destinationCardNumber, amount, pin);
            System.out.printf("Successfully transferred $%.2f to card ending with %s%n",
                    amount, destinationCardNumber.substring(destinationCardNumber.length() - 4));
        } catch (AccountNotFoundException | InsufficientFundsException | InvalidAmountException | SecurityException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }

        ConsoleHelper.pause("");
    }

    private static void viewTransactionHistory() {
        ConsoleHelper.printMenuHeader("TRANSACTION HISTORY");

        try {
            List<Transaction> transactions = accountService.getTransactionHistory(currentAccount.getCardNumber());

            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
            } else {
                System.out.println("Recent transactions:");
                for (int i = transactions.size() - 1; i >= 0; i--) {
                    System.out.println((transactions.size() - i) + ". " + transactions.get(i));
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        ConsoleHelper.pause("");
    }

    private static void logout() {
        currentAccount = null;
        System.out.println("You have been logged out successfully.");
        ConsoleHelper.pause("");
    }
}