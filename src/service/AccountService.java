package service;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;

import java.util.List;

public interface AccountService {
    void createAccount(String cardNumber, String ownerName, double initialBalance, String pin);

    Account getAccountByCardNumber(String cardNumber) throws AccountNotFoundException;

    List<Account> getAllAccounts();

    void transferMoney(String sourceCardNumber, String destinationCardNumber, double amount, String pin)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException;

    List<Transaction> getTransactionHistory(String cardNumber) throws AccountNotFoundException;

    boolean authenticateAccount(String cardNumber, String pin) throws AccountNotFoundException;
}