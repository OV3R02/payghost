package it.tsp.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mysql.cj.xdevapi.Statement;

import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Store {

    private static String URL = "jdbc:mysql://127.0.0.1:3306/?user=payghost";
    private static String USER = "payghost";
    private static String PWD = "payghost";
    
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

    // Annulla la transazione
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

    public static List<Transaction> findTransactionByAccount(long accountId) {
        return em.createNamedQuery(Transaction.FIND_BY_ACCOUNT_ID, Transaction.class)
                    .setParameter("id", accountId)
                    .getResultList();
    }

    public static List<Transaction> jpaFindTransactionByUserId(long accountId){
        return em.createNamedQuery(Transaction.FIND_BY_ACCOUNT_ID, Transaction.class)
                .setParameter("ID", accountId)
                .getResultList();
    }
}