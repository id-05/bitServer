package ru.CustomOrthancWebMorda.beans;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.CustomOrthancWebMorda.beans.dao.BitServerStudy;
import ru.CustomOrthancWebMorda.beans.dao.BitServerDBresources;
import ru.CustomOrthancWebMorda.beans.dao.Usergroup;
import ru.CustomOrthancWebMorda.beans.dao.Users;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Users.class);
                configuration.addAnnotatedClass(Usergroup.class);
                configuration.addAnnotatedClass(BitServerDBresources.class);
                configuration.addAnnotatedClass(BitServerStudy.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
