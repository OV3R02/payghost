package it.tsp.control;

import java.util.List;

import it.tsp.entity.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class TransactionStore {

    @PersistenceContext(unitName = "payghost")
    private EntityManager em;

    public  Transaction saveTransaction(Transaction tr) {
       Transaction saved = this.em.merge(tr);
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
