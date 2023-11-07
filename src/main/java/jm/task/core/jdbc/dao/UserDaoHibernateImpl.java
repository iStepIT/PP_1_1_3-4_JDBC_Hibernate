package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;

    public UserDaoHibernateImpl () {
    }

    @Override
    public void createUsersTable () {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("CREATE TABLE IF NOT EXISTS User (" +
                            "id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR (50) NOT NULL," +
                            "lastName VARCHAR (50) NOT NULL," +
                            "age TINYINT NOT NULL)")
                    .executeUpdate ();

            session.getTransaction ().commit ();
            System.out.println ("Table CREATE");
        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Table CREATE error!");
        }
    }

    @Override
    public void dropUsersTable () {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("DROP TABLE if EXISTS User").executeUpdate ();
            session.getTransaction ().commit ();

            System.out.println ("Table DROP");

        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Table DROP error!");
        }
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.save (new User (name, lastName, age));
            session.getTransaction ().commit ();

            System.out.println ("User SAVE");
            System.out.println ("User : " + name + " " + lastName + " " + age + " was saved");
        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Table SAVE error!");
        }
    }

    @Override
    public void removeUserById (long id) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.delete (session.get (User.class, id));
            session.getTransaction ().commit ();

            System.out.println ("User DELETED by ID");
        } catch ( Exception e ) {
            System.out.println ("Table removeUserById error!");
        }
    }

    @Override
    public List<User> getAllUsers () {
        List<User> userList = null;
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            userList = session.createQuery ("FROM User").getResultList ();
            session.getTransaction ().commit ();
            System.out.println ("Users GET ALL");

        } catch ( Exception e ) {
            System.out.println ("Users getAllUsers error!");
        }
        return userList;
    }

    @Override
    public void cleanUsersTable () {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("DELETE FROM User")
                    .executeUpdate ();
            session.getTransaction ().commit ();
            System.out.println ("Users DELETED");
        } catch ( Exception e ) {
            System.out.println ("Users cleanUsersTable error!");
        }
    }
}