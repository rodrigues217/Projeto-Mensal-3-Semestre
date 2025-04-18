package org.example.Util;

import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Factory {

    private static final EntityManagerFactory emf;

static {
    SessionFactory sessionFactory =  new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();
    emf = sessionFactory.unwrap(EntityManagerFactory.class);

}

public static EntityManager getEntityManager(){
    return emf.createEntityManager();

}
public static void fechar (){
    emf.close();
    }

}
