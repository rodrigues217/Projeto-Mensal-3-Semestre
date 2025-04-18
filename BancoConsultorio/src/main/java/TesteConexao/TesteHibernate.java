package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TesteHibernate {
    private SessionFactory sessionFactory;
    private Session session;

    public TesteHibernate() {
        // Construtor vazio, mas você pode adicionar configs customizadas se quiser depois
    }

    public void conectar() {
        try {
            Configuration cfg = new Configuration().configure();
            sessionFactory = cfg.buildSessionFactory();
            session = sessionFactory.openSession();



        } catch (Exception e) {

        }
    }

    public void fechar() {
        if (session != null) session.close();
        if (sessionFactory != null) sessionFactory.close();

    }

    // Getters e Setters
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}