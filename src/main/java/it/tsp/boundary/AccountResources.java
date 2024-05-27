package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.Objects;
import it.tsp.control.*;
import it.tsp.control.RechargeStore;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@ApplicationScoped
@Path("/accounts")
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class AccountResources {

    
    @Inject
    AccountStore accountStore;

    @Inject
    RechargeStore rechargeStore;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registration(Account account){
        try {
               if (!Objects.equals(account.getPwd(), account.getConfirmPwd())) {
                    throw new RegistrationException("The two passwords doesn't match!");
               }

               //Account a1 = new Account(account.getFname(), account.getLname(), account.getPwd(), account.getEmail());

               Account saved = accountStore.saveAccount(account);

               if (account.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                    Recharge recharge = new Recharge(saved, account.getCredit());
                    @SuppressWarnings("unused")
                    Recharge r = rechargeStore.saveRecharge(recharge);
                    saved.setCredit(account.getCredit());
                    accountStore.saveAccount(saved);
               }
               return Response.ok().build();
          } catch (Exception e) {
               throw new RegistrationException(e.getMessage());
          }
    }


}
