package it.tsp.control;

import it.tsp.entity.Account;
import it.tsp.entity.Recharge;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Store {
    
        
        private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        private static EntityManager em = emf.createEntityManager();

        //costruttore static 
        {
            
        }

        public static Account saveAccount(Account e){
            em.getTransaction().begin();
            Account saved = em.merge(e);
            em.getTransaction().commit();
            return saved;
        }

        public static Recharge saveRecharge(Recharge recharge) {
           em.getTransaction().begin();
           Recharge saved = em.merge(recharge);
           em.getTransaction().commit();
           return saved;
        }

        
    }
