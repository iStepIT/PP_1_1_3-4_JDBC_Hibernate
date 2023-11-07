package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    private Transaction transaction = null;

    public UserDaoHibernateImpl () {
    }

    @Override
    public void createUsersTable () {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.createSQLQuery ("CREATE TABLE IF NOT EXISTS User (" +
                            "id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR (50) NOT NULL," +
                            "lastName VARCHAR (50) NOT NULL," +
                            "age TINYINT NOT NULL)")
                    .executeUpdate ();

            System.out.println ("Создали таблицу");
        } catch ( HibernateException e ) {
            transaction.rollback ();
            e.printStackTrace ();
            System.out.println ("Не удалось создать таблицу");
        }
    }

    @Override
    public void dropUsersTable () {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("DROP TABLE if EXISTS User").executeUpdate ();
            session.getTransaction ().commit ();

            System.out.println ("Таблица удалена");

        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Не удалилось удалить таблицу");
        }
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            transaction = session.beginTransaction ();
            session.beginTransaction ();
            session.save (new User (name, lastName, age));
            transaction.commit ();

            System.out.println ("User c именем: " + name + " добавлен в базу данных");
        } catch ( HibernateException e ) {
            transaction.rollback ();
            e.printStackTrace ();
            System.out.println ("Пользователь не сохранился!");
        }
    }

    @Override
    public void removeUserById (long id) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            transaction = session.beginTransaction ();
            session.beginTransaction ();
            session.delete (session.get (User.class, id));
            transaction.commit ();

            System.out.println ("Пользователь c " + id + "удалился");
        } catch ( Exception e ) {
            transaction.rollback ();
            System.out.println ("Пользователь не удалился");
        }
    }

    @Override
    public List<User> getAllUsers () {
        List<User> userList = null;
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            userList = session.createQuery ("FROM User").getResultList ();
            session.getTransaction ().commit ();
            System.out.println ("Все пользователи");

        } catch ( Exception e ) {
            System.out.println ("Не удалось вывести юзеров!");
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
            System.out.println ("таблица отчищена");
        } catch ( Exception e ) {
            System.out.println ("таблица не отчищена!");
        }
    }
}
