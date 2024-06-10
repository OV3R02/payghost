package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.Objects;
import it.tsp.control.*;
import it.tsp.control.RechargeStore;
import it.tsp.dto.CreateRechargeDTO;
import it.tsp.dto.CreateTransactionDTO;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
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

    @Inject
    TransactionStore transactionStore;

    @Inject
    PayghostManager payghostManager;
    
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
        Account result = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));
        return Response.ok(result).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/recharges")
    public Response doRecharge(@PathParam("id") long id, @Valid CreateRechargeDTO e) {
        Account account = accountStore.findAccountById(id).orElseThrow(() -> new NotFoundException("Account not found"));
        Recharge toSave = new Recharge(account, e.amounth());
        Recharge saved = rechargeStore.saveRecharge(toSave);
        account.increaseCredit(e.amounth());
        accountStore.saveAccount(account);
        return Response.status(Status.CREATED)
        .entity(saved)
        .build();
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/recharges")
    public Response allRecharges(@PathParam("id") long id) {
        @SuppressWarnings("unused")
        Account account = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return Response.ok(rechargeStore.findRechargesByAccountId(id)).build();
        
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(value = TxType.NOT_SUPPORTED)
    @Path("/{id}/transactions")
    public Response doTransaction(@PathParam("id") long id, @Valid CreateTransactionDTO e) {
        Account senderAccount = accountStore.findAccountById(id)
            .orElseThrow(() -> new NotFoundException("Sender account not exist."));
        Account receiverAccount = accountStore.findAccountById(e.receiverId())
            .orElseThrow(() -> new NotFoundException("Receiver account not exist."));
        Transaction tx = payghostManager.doTransaction(senderAccount, receiverAccount, e.amounth());
        return Response.status(Status.CREATED)
        .entity(tx)
        .build();

    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/transactions")
    public Response allTransactions(@PathParam("id") long id) {
        @SuppressWarnings("unused")
        Account account = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return Response.ok(transactionStore.findTransactionByAccount(id))
        .build();
    }


}
