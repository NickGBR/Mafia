package org.dreamteam.mafia;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBwrapper {
    public static void main(String[] args) {

        try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
             Session session = factory.getCurrentSession();) {

            session.beginTransaction();


            session.getTransaction().commit();

        }

    }
}
