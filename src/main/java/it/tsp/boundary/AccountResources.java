package it.tsp.boundary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;

import it.tsp.control.*;
import it.tsp.dto.AccountSlice;
import it.tsp.dto.CreateRechargeDTO;
import it.tsp.dto.CreateTransactionDTO;
import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.Transaction;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response.Status;


@ApplicationScoped
@Path("/accounts")
public class AccountResources {
    
    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityContext ctx;
    
    @Inject
    AccountStore accountStore;
    
    @Inject
    RechargeStore rechargeStore;

    @Inject
    TransactionStore transactionStore;

    @Inject
    PayghostManager payghostManager;
    
    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Response registration(@Valid Account account){
               if (!Objects.equals(account.getPwd(), account.getConfirmPwd())) {
                    throw new PayghostException("The two passwords doesn't match!");
               }

               account.setPwd(EncodeUtils.encode(account.getPwd()));
               
               Account saved = accountStore.saveAccount(account);

               if (account.getCredit().compareTo(BigDecimal.ZERO) > 0) {
                    Recharge recharge = new Recharge(saved, account.getCredit());
                    rechargeStore.saveRecharge(recharge);
               }
               return Response
                    .status(Status.CREATED)
                    .entity(saved.getID())
                    .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllAccounts(){
        System.out.println(ctx.isUserInRole("USERS"));
        List<Account> result = accountStore.findAll();
        List<AccountSlice> resConverted = result.stream()
         .map(v -> new AccountSlice(v.getID(), v.getFname(), v.getLname()))
         .collect(Collectors.toList());
            
        return Response.ok(resConverted).build();
    }

    @RolesAllowed("USERS")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response findAccountById(@PathParam("id") long id){
        checkUserSecurity(id);
        Account result = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account doesn't exist!"));
        return Response.ok(result).build();
    }

    @RolesAllowed("USERS")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/recharges")
    public Response doRecharge(@PathParam("id") long id, @Valid CreateRechargeDTO e) {
        System.out.println("prova");
        checkUserSecurity(id);
        Account account = accountStore.findAccountById(id).orElseThrow(() -> new NotFoundException("Account not found"));
        Recharge toSave = new Recharge(account, e.amounth());
        Recharge saved = rechargeStore.saveRecharge(toSave);
        account.increaseCredit(e.amounth());
        accountStore.saveAccount(account);
        return Response.status(Status.CREATED)
        .entity(saved)
        .build();
    }
    
    @RolesAllowed("USERS")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/recharges")
    public Response allRecharges(@PathParam("id") long id) {
        checkUserSecurity(id);

        @SuppressWarnings("unused")
        Account account = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return Response.ok(rechargeStore.findRechargesByAccountId(id)).build();
        
    }

    @RolesAllowed("USERS")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(value = TxType.NOT_SUPPORTED)
    @Path("/{id}/transactions")
    public Response doTransaction(@PathParam("id") long id, @Valid CreateTransactionDTO e) {
        checkUserSecurity(id);
        Account senderAccount = accountStore.findAccountById(id)
            .orElseThrow(() -> new NotFoundException("Sender account not exist."));
        Account receiverAccount = accountStore.findAccountById(e.receiverId())
            .orElseThrow(() -> new NotFoundException("Receiver account not exist."));
        Transaction tx = payghostManager.doTransaction(senderAccount, receiverAccount, e.amounth());
        return Response.status(Status.CREATED)
        .entity(tx)
        .build();

    }
    
    @RolesAllowed("USERS")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/transactions")
    public Response allTransactions(@PathParam("id") long id) {
        checkUserSecurity(id);
        @SuppressWarnings("unused")
        Account account = accountStore.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return Response.ok(transactionStore.findTransactionByAccount(id))
        .build();
    }

     private void checkUserSecurity(long userPathId) {
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) {
                throw new PayghostException("security information not valid");
        }

        if (!String.valueOf(userPathId).equals(subject)) {
                throw new PayghostException("attempt to access not owned resources");
        }
    }


}
