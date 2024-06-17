package it.tsp.control;

import java.math.BigDecimal;
import it.tsp.boundary.PayghostException;
import it.tsp.dto.CredentialDTO;
import it.tsp.entity.Account;
import it.tsp.entity.Transaction;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

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


    public String doLogin(@Valid CredentialDTO e) {
        Account account = accountStore.findAccountByUsr(e.email())
        .orElseThrow(() -> new PayghostException("login failed"));
        System.out.println();
        if ( !EncodeUtils.verify(e.pwd(), account.getPwd())) {
            throw new PayghostException("login failed");
        }
        return String.valueOf(account.getID());
    }
}
