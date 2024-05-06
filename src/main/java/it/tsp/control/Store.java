package it.tsp.control;

import java.math.BigDecimal;
import java.util.Optional;

import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.RechargeException;
import it.tsp.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Store {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    private static EntityManager em = emf.createEntityManager();

    // costruttore static
    {

    }

    public static void beginTran() {
        if (em.getTransaction().isActive()) {
            throw new StoreException("Esiste già una transazione attiva");
        }
        em.getTransaction().begin();
        
    }

    public static void commitTran() {
        if (!em.getTransaction().isActive()) {
            throw new StoreException("Nessuna transazione attiva");
        }
        em.getTransaction().commit();
    }

    public static void rollTran() {
        if (!em.getTransaction().isActive()) {
            throw new StoreException("Nessuna transazione attiva");
        }
        em.getTransaction().rollback();
    }

    public static Account saveAccount(Account e) {
        if (em.getTransaction().isActive()) {
            return em.merge(e);
        }
        em.getTransaction().begin();
        Account saved = em.merge(e);
        em.getTransaction().commit();
        return saved;
    }

    public static Recharge saveRecharge(Recharge e) {
        if (em.getTransaction().isActive()) {
            return em.merge(e);
        }
        em.getTransaction().begin();
        Recharge saved = em.merge(e);
        em.getTransaction().commit();
        return saved;
    }

    public static Optional<Account> findAccountById(long accountId) {
        Account account = em.find(Account.class, accountId);
        return account==null ? Optional.empty() : Optional.of(account);
    }

    public static Transaction saveTransaction(Transaction tr) {
       if (em.getTransaction().isActive()) {
            return em.merge(tr);
            
       }
       em.getTransaction().begin();
       Transaction saved = em.merge(tr);
       em.getTransaction().commit();
       return saved;
       
    }
}