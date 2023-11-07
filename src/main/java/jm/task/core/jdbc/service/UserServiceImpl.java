package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDaoHibernateImpl = new UserDaoHibernateImpl ();

    @Override
    public void createUsersTable () {
        userDaoHibernateImpl.createUsersTable ();
    }

    @Override
    public void dropUsersTable () {
        userDaoHibernateImpl.dropUsersTable ();
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        userDaoHibernateImpl.saveUser (name, lastName, age);
        System.out.println ("User с именем " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById (long id) {
        userDaoHibernateImpl.removeUserById (id);
    }

    @Override
    public List<User> getAllUsers () {
        return userDaoHibernateImpl.getAllUsers ();
    }

    @Override
    public void cleanUsersTable () {
        userDaoHibernateImpl.cleanUsersTable ();
    }
}