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
            System.out.println ("Таблица создана");
        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Ошибка созданния таблицы");
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
            System.out.println ("НЕ удалилось удалить таблицу");
        }
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.save (new User (name, lastName, age));
            session.getTransaction ().commit ();

            System.out.println ("User c именем: " + name + " добавлен в базу данных");
        } catch ( HibernateException e ) {
            e.printStackTrace ();
            System.out.println ("Пользователь НЕ сохранился!");
        }
    }

    @Override
    public void removeUserById (long id) {
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            session.delete (session.get (User.class, id));
            session.getTransaction ().commit ();

            System.out.println ("Пользователь с id: " + id + " удален");
        } catch ( Exception e ) {
            System.out.println ("Пользователь с id: " + id + " НЕ удалился");
        }
    }

    @Override
    public List<User> getAllUsers () {
        List<User> userList = null;
        try (Session session = Util.getConnectionHibernate ().openSession ()) {
            session.beginTransaction ();
            userList = session.createQuery ("FROM User").getResultList ();
            session.getTransaction ().commit ();

        } catch ( Exception e ) {
            System.out.println ("НЕ удалось вывести юзеров!");
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
            System.out.println ("Отчистка таблицы выполнена");
        } catch ( Exception e ) {
            System.out.println ("Отчистка таблицы НЕ выполнена");
        }
    }
}