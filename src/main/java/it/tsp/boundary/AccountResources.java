package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import it.tsp.control.*;
import it.tsp.control.RechargeStore;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@ApplicationScoped
@Path("/accounts")
public class AccountResources {
    
    
    @Inject
    AccountStore accountStore;
    
    @Inject
    RechargeStore rechargeStore;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
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
               return Response
                    .status(Status.CREATED)
                    .entity(saved.getID())
                    .build();
                    
          } catch (Exception e) {
               throw new RegistrationException(e.getMessage());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllAccounts(){
        return Response.ok(accountStore.findAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response findAccountById(@PathParam("id") long id){
        Optional<Account> result = accountStore.findAccountById(id);
        return result.isPresent() ? Response.ok(result.get()).build()
            : Response.status(Status.NOT_FOUND).build();
    }

}
