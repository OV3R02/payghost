package it.tsp;

import java.math.BigDecimal;
import java.util.Set;

import it.tsp.boundary.PayGhost;
import it.tsp.boundary.RegistrationException;
import it.tsp.control.Store;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import jakarta.validation.*;



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

        Account saved = PayGhost.registration(
            "Omar", 
            "Vergnano", 
            "omarverg37@gmail.com", 
            "1234", 
            "1234", 
            BigDecimal.valueOf(200));

        System.out.println(saved);

        Recharge rc = PayGhost.doRecharge(1, BigDecimal.valueOf(1000));
    }
}
