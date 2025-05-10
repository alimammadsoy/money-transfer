package service;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Transaction;
import model.TransactionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountServiceImpl implements AccountService {
    private final Map<String, Account> accountsByCardNumber = new HashMap<>();
    private final TransactionService transactionService = new TransactionServiceImpl();

    @Override
    public void createAccount(String cardNumber, String ownerName, double initialBalance, String pin) {
        Account account = new Account(cardNumber, ownerName, initialBalance, pin);
        accountsByCardNumber.put(cardNumber, account);

        // Create initial deposit transaction if there's an initial balance
        if (initialBalance > 0) {
            Transaction transaction = new Transaction(
                    null,
                    account.getAccountId(),
                    initialBalance,
                    TransactionType.DEPOSIT,
                    "Initial deposit"
            );
            account.addTransaction(transaction);
        }

    }

    @Override
    public Account getAccountByCardNumber(String cardNumber) throws AccountNotFoundException {
        Account account = accountsByCardNumber.get(cardNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account with card number " + cardNumber + " not found");
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountsByCardNumber.values());
    }

    @Override
    public void transferMoney(String sourceCardNumber, String destinationCardNumber, double amount, String pin)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero");
        }

        Account sourceAccount = getAccountByCardNumber(sourceCardNumber);

        if (!sourceAccount.validatePin(pin)) {
            throw new SecurityException("Invalid PIN");
        }

        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds to complete the transfer");
        }

        Account destinationAccount = getAccountByCardNumber(destinationCardNumber);

        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);

        transactionService.recordTransfer(sourceAccount, destinationAccount, amount);
    }

    @Override
    public List<Transaction> getTransactionHistory(String cardNumber) throws AccountNotFoundException {
        Account account = getAccountByCardNumber(cardNumber);
        return account.getTransactionHistory();
    }

    @Override
    public boolean authenticateAccount(String cardNumber, String pin) throws AccountNotFoundException {
        Account account = getAccountByCardNumber(cardNumber);
        return account.validatePin(pin);
    }
}