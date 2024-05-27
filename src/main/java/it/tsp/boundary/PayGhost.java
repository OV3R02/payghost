package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import it.tsp.control.AccountStore;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.RechargeException;
import it.tsp.entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PayGhost {

     @Inject
     AccountStore accountStore;

     public  Account registration(String fname, String lname, String email, String pwd, String confirmPwd,
               BigDecimal credit) {

          try {
               if (!Objects.equals(pwd, confirmPwd)) {
                    throw new RegistrationException("The two passwords doesn't match!");
               }

               Account account = new Account(fname, lname, pwd, email);

               Account saved = accountStore.saveAccount(account);

               if (credit.compareTo(BigDecimal.ZERO) > 0) {
                    Recharge recharge = new Recharge(saved, credit);
                    @SuppressWarnings("unused")
                    Recharge r = accountStore.saveRecharge(recharge);
                    saved.setCredit(credit);
                    accountStore.saveAccount(saved);
               }
               return saved;
          } catch (Exception e) {
               throw new RegistrationException(e.getMessage());
          }

     }

     public void recharge(long accountId, BigDecimal amount) {
          try {
               // Find account id
               Account found = accountStore.findAccountById(accountId)
                    .orElseThrow(() -> new RechargeException("Account not found!"));

               // Recharge saved based on amount gave to account
               accountStore.saveRecharge(new Recharge(found, amount));

               // increase account credit based on amount
               found.increaseCredit(amount);
               
           } catch (Exception e) {
               throw new RechargeException("Account not found!");
          }

     }

     public void sendMoney(long accountId1, long accountId2, BigDecimal amount) {
          try {
               // Finds the sender id
               Account senderAccount = accountStore.findAccountById(accountId1)
                    .orElseThrow(() -> new TransactionException("Sender account not found!"));

               // Finds the sender id
               Account receiverAccount = accountStore.findAccountById(accountId2)
                    .orElseThrow(() -> new TransactionException("Receiver account not found!"));

               accountStore.saveTransaction(new Transaction(senderAccount, receiverAccount, amount));
               
               // Increase credit on receiver account base on the amount 
               receiverAccount.increaseCredit(amount);

               // decrease credit on sender account based on amount
               senderAccount.decreaseCredit(amount);


          } catch (Exception e) {
               System.out.println(e);
               throw new TransactionException("Transaction failed!");
          }
     }

     public List<Transaction> transactionByUser(long accountId) {
          return accountStore.findTransactionByAccount(accountId);
     }

    public static Iterable<Transaction> rechargeByUser(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'rechargeByUser'");
    }
}