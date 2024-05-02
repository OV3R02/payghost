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
               // nuovo oggetto recharge
               Account found = Store.findAccountById(accountId).orElseThrow(() -> new RechargeException("Account not found!"));
           } catch (Exception e) {
               throw new RechargeException("Account not found!");
           }
     }

     public static void doTransaction(long accountId, BigDecimal amount) {
          try {
               
          } catch (Exception e) {
               throw new TransactionException("Transaction failed!");
          }
     }

     public static List<Transaction> transactionByUser(long accountId) {
          throw new UnsupportedOperationException("not implement yet..");
     }

}