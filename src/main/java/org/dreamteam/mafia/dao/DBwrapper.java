package org.dreamteam.mafia.dao;

import org.dreamteam.mafia.dao.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBwrapper {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU");
        EntityManager em = emf.createEntityManager();

        User u = new User();

        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();

    }
}
