package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/data";
    private static final String USERNAME = "root";
    private static final String PASS = "35312324";

    public static Connection getConnection () {
        Connection connection = null;
        try {
            Class.forName (DRIVER);
            connection = DriverManager.getConnection (URL, USERNAME, PASS);
            System.out.println ("Коннект ОК");
        } catch ( ClassNotFoundException | SQLException e ) {
            e.printStackTrace ();
            System.out.println ("Проблема подключения к БД");
        }
        return connection;

    }

    //Hibernate
    private static SessionFactory sessionFactory;

    public static SessionFactory getConnectionHibernate () {
        if (sessionFactory == null) {
            try {
                Properties settings = new Properties ();
                settings.put (Environment.URL, URL + "?useSSL=false");
                settings.put (Environment.USER, USERNAME);
                settings.put (Environment.PASS, PASS);
                settings.put (Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put (Environment.SHOW_SQL, "true");
                settings.put (Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put (Environment.HBM2DDL_AUTO, "create");

                sessionFactory = new Configuration ()
                        .addProperties (settings)
                        .addAnnotatedClass (User.class)
                        .buildSessionFactory ();

                System.out.println ("Connection SUCCESS");
            } catch ( Exception e ) {
                e.printStackTrace ();
                System.out.println ("Connection ERROR");
            }
        }
        return sessionFactory;
    }
}