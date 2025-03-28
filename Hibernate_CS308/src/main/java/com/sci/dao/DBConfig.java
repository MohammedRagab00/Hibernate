package com.sci.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConfig {

    public static final SessionFactory SESSION_FACTORY = new Configuration()
            .configure()
            .buildSessionFactory();

    public static void shutdown() {
        if (SESSION_FACTORY.isOpen()) {
            SESSION_FACTORY.close();
        }
    }
}
