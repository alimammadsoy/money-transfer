package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private final String accountId;
    private final String cardNumber;
    private final String ownerName;
    private double balance;
    private final List<Transaction> transactions;
    private final String pin;

    public Account(String cardNumber, String ownerName, double initialBalance, String pin) {
        this.accountId = UUID.randomUUID().toString();
        this.cardNumber = cardNumber;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.pin = pin;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean validatePin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardNumber='" + maskCardNumber(cardNumber) + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                '}';
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() < 8) return cardNumber;
        return cardNumber.substring(0, 4) + "********" + cardNumber.substring(cardNumber.length() - 4);
    }
}