package it.tsp.control;
import java.util.Optional;

import it.tsp.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class AccountStore {
    
    @PersistenceContext(unitName = "payghost")
    private EntityManager em;

    public Account saveAccount(Account e) {
        Account saved = em.merge(e);
        return saved;
    }

    public Optional<Account> findAccountById(long accountId) {
        Account account = em.find(Account.class, accountId);
        return account==null ? Optional.empty() : Optional.of(account);
    }

    
}