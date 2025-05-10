package service;

import model.Account;
import model.Transaction;
import model.TransactionType;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public void recordTransfer(Account source, Account destination, double amount) {
        Transaction sentTransaction = new Transaction(
                source.getAccountId(),
                destination.getAccountId(),
                amount,
                TransactionType.TRANSFER_SENT,
                "Transfer to " + destination.getCardNumber().substring(destination.getCardNumber().length() - 4)
        );
        source.addTransaction(sentTransaction);

        Transaction receivedTransaction = new Transaction(
                source.getAccountId(),
                destination.getAccountId(),
                amount,
                TransactionType.TRANSFER_RECEIVED,
                "Transfer from " + source.getCardNumber().substring(source.getCardNumber().length() - 4)
        );
        destination.addTransaction(receivedTransaction);
    }
}