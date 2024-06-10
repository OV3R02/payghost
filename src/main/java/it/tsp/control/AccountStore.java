package it.tsp.control;
import java.util.List;
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

    public List<Account> findAll(){
        return em.createNamedQuery(Account.FIND_ALL, Account.class).getResultList();
    }

    public Optional<Account> findAccountByUsr(String email) {
        List<Account> result = em.createNamedQuery(Account.FIND_BY_USER, Account.class)
            .setParameter("email", email)
            .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));

    }
}