package it.tsp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import it.tsp.boundary.PayGhost;
import it.tsp.control.Store;
import it.tsp.entity.Account;
import it.tsp.entity.Transaction;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) 
    {

        /*ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Account>> result = validator.validate(a);
        result.forEach(v -> System.out.println(v));*/

        Account saved1 = PayGhost.registration(
            "Omar", 
            "Vergnano", 
            "omarverg37@gmail.com", 
            "1234", 
            "1234", 
            BigDecimal.valueOf(200));

        Account saved2 = PayGhost.registration(
            "Marco", 
            "Ballarin", 
            "mb@gmail.com", 
            "1234", 
            "1234", 
            BigDecimal.valueOf(10));

        PayGhost.recharge(saved1.getID(), BigDecimal.valueOf(1000));
        PayGhost.recharge(saved2.getID(), BigDecimal.valueOf(100));

        PayGhost.sendMoney(saved1.getID(), saved2.getID(), BigDecimal.valueOf(1000));

        System.out.println(saved1);
        System.out.println(saved2);

        PayGhost.transactionByUser(saved1.getID()).forEach(v -> System.out.println(String.format("sender: %s, receiver: %s, amount: %s",
                    v.getSenderAccount().getFullName(), 
                    v.getReceiverAccount().getFullName(), 
                    v.viewAmount(saved1.getID()))));

        List<Transaction> ftbu = Store.jpaFindTransactionByUserId(1);
    }
}
