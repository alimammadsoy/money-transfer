package service;

import model.Account;

public interface TransactionService {
    void recordTransfer(Account source, Account destination, double amount);
}


