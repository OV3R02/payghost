package it.tsp.boundary;
import java.math.BigDecimal;
import it.tsp.control.AccountStore;
import it.tsp.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PayGhost {

     @Inject
     AccountStore accountStore;

     public Account registration(String fname, String lname, String email, String pwd, String confirmPwd,
               BigDecimal credit) {

          throw new UnsupportedOperationException();

     }

     /*public void recharge(long accountId, BigDecimal amount) {
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
          //return accountStore.findTransactionByAccount(accountId);
          throw new UnsupportedOperationException();
     }

    public static Iterable<Transaction> rechargeByUser(long id) {
        throw new UnsupportedOperationException("Unimplemented method 'rechargeByUser'");
    }*/
}