package it.tsp.control;

import java.math.BigDecimal;

import it.tsp.boundary.PayghostException;
import it.tsp.entity.Account;
import it.tsp.entity.Transaction;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class PayghostManager {
    
    @Inject
    AccountStore accountStore;

    @Inject
    TransactionStore transactionStore;

    public Transaction doTransaction(Account senderAccount, Account receiverAccount, BigDecimal amount) {
       
        
        if (!senderAccount.hasSufficientCredit(amount)) {
            throw new PayghostException("insufficient credit!");
        }
        
        Transaction toSave = new Transaction(senderAccount, receiverAccount, amount);
        Transaction saved = transactionStore.saveTransaction(toSave);
        senderAccount.decreaseCredit(amount);
        receiverAccount.increaseCredit(amount);
        accountStore.saveAccount(senderAccount);
        accountStore.saveAccount(receiverAccount);
        return saved;
    }
}
