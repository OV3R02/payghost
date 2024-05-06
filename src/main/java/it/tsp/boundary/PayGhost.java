package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.tsp.control.Store;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.RechargeException;
import it.tsp.entity.Transaction;

public class PayGhost {

     public static Account registration(String fname, String lname, String email, String pwd, String confirmPwd,
               BigDecimal credit) {

          try {
               if (!Objects.equals(pwd, confirmPwd)) {
                    throw new RegistrationException("The two passwords doesn't match!");
               }

               Store.beginTran();

               Account account = new Account(fname, lname, pwd, email);

               Account saved = Store.saveAccount(account);

               if (credit.compareTo(BigDecimal.ZERO) > 0) {
                    Recharge recharge = new Recharge(saved, credit);
                    @SuppressWarnings("unused")
                    Recharge r = Store.saveRecharge(recharge);
                    saved.setCredit(credit);
                    Store.saveAccount(saved);
               }
               Store.commitTran();
               return saved;
          } catch (Exception e) {
               Store.rollTran();
               throw new RegistrationException(e.getMessage());
          }

     }

     public static void doRecharge(long accountId, BigDecimal amount) {
          try {
               // Find account id
               Account found = Store.findAccountById(accountId)
                    .orElseThrow(() -> new RechargeException("Account not found!"));
               
               // Opens transaction
               Store.beginTran();

               // Recharge saved based on amount gave to account
               Store.saveRecharge(new Recharge(found, amount));

               // increase account credit based on amount
               found.increaseCredit(amount);

               // Saves account info
               Store.saveAccount(found);

               // Closes transaction
               Store.commitTran();
               
           } catch (Exception e) {
               Store.rollTran();
               throw new RechargeException("Account not found!");
          }

     }

     public static void doTransaction(long accountId1, long accountId2, BigDecimal amount) {
          try {
               // Finds the sender id
               Account senderAccount = Store.findAccountById(accountId1)
                    .orElseThrow(() -> new TransactionException("Sender account not found!"));

               // Finds the sender id
               Account receiverAccount = Store.findAccountById(accountId2)
                    .orElseThrow(() -> new TransactionException("Receiver account not found!"));

               // Open new transaction
               Store.beginTran();

               Store.saveTransaction(new Transaction(senderAccount, receiverAccount, amount));
               
               // Increase credit on receiver account base on the amount 
               receiverAccount.increaseCredit(amount);

               // decrease credit on sender account based on amount
               senderAccount.decreaseCredit(amount);

               // Save both accounts
               Store.saveAccount(senderAccount);
               Store.saveAccount(receiverAccount);

               // Close transaction
               Store.commitTran();

          } catch (Exception e) {
               System.out.println(e);
               Store.rollTran();
               throw new TransactionException("Transaction failed!");
          }
     }

     public static List<Transaction> transactionByUser(long accountId) {
          throw new UnsupportedOperationException("not implement yet..");
     }

}