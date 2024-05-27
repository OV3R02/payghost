package it.tsp.control;
import java.util.List;
import java.util.Optional;

import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import it.tsp.entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class AccountStore {
    
    @PersistenceContext(unitName = "payghost")
    private EntityManager em = null;

    public Account saveAccount(Account e) {
        Account saved = em.merge(e);
        return saved;
    }

    public Recharge saveRecharge(Recharge e) {
        Recharge saved = em.merge(e);
        return saved;
    }

    public Optional<Account> findAccountById(long accountId) {
        Account account = em.find(Account.class, accountId);
        return account==null ? Optional.empty() : Optional.of(account);
    }

    public  Transaction saveTransaction(Transaction tr) {
       
       Transaction saved = em.merge(tr);
       return saved;
       
    }

    public List<Transaction> findTransactionByAccount(long accountId) {
        return em.createNamedQuery(Transaction.FIND_BY_ACCOUNT_ID, Transaction.class)
                    .setParameter("id", accountId)
                    .getResultList();
    }

    public List<Transaction> jpaFindTransactionByUserId(long accountId){
        return em.createNamedQuery(Transaction.FIND_BY_ACCOUNT_ID, Transaction.class)
                .setParameter("ID", accountId)
                .getResultList();
    }
}