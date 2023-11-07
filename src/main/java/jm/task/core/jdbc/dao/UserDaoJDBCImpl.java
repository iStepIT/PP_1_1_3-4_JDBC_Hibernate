package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Transaction;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserDaoJDBCImpl implements UserDao {
    private static Connection connection = Util.getConnection ();
    private Transaction transaction = null;


    public UserDaoJDBCImpl () {
    }

    @Override
    public void createUsersTable () {
// Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
        try (Statement statement = connection.createStatement ()) {
            String sql = "CREATE TABLE IF NOT EXISTS users(id bigint AUTO_INCREMENT PRIMARY KEY, name varchar(100) NOT NULL, lastName varchar(100) NOT NULL, age tinyint DEFAULT 0)";
            statement.executeUpdate (sql);
            System.out.println ("Создали таблицу");

        } catch ( SQLException e ) {
            System.out.println ("Не удалось создать таблицу");
        }
    }

    @Override
    public void dropUsersTable () {
        //Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
        try (Statement statement = connection.createStatement ()) {
            String sql = "DROP TABLE IF EXISTS Users";
            statement.execute (sql);
            System.out.println ("Таблица удалена");
        } catch ( SQLException e ) {
            e.printStackTrace ();
            System.out.println ("Не удалилось удалить таблицу");
        }
    }

    @Override
    public void saveUser (String name, String lastName, byte age) {
        // Добавление User в таблицу
        String sql = "INSERT INTO users ( name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement (sql);) {
            connection.setAutoCommit (false);

            preparedStatement.setString (1, name);
            preparedStatement.setString (2, lastName);
            preparedStatement.setByte (3, age);
            preparedStatement.executeUpdate ();

            connection.commit ();

            System.out.println ("User c именем: " + name + " добавлен в базу данных");

        } catch ( SQLException e ) {
            try {
                connection.rollback ();
            } catch ( SQLException ex ) {
                ex.printStackTrace ();
            }
            System.out.println ("Пользователь не сохранился");
        }
    }

    @Override
    public void removeUserById (long id) {
        //Удаление User из таблицы ( по id )
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement (sql)) {
            connection.setAutoCommit (false);
            preparedStatement.setLong (1, id);
            preparedStatement.executeUpdate ();
            connection.commit ();

        } catch ( SQLException e ) {
            try {
                connection.rollback ();
            } catch ( SQLException ex ) {
                ex.printStackTrace ();
            }
            System.out.println ("Пользователь не удалился");
        }
    }

    @Override
    public List<User> getAllUsers () {
        //Получение всех User(ов) из таблицы
        List<User> result = new ArrayList<> ();
        String sql = "SELECT * FROM users";

        try (PreparedStatement preparedStatement = connection.prepareStatement (sql);
             ResultSet resultSet = preparedStatement.executeQuery ()) {
            while (resultSet.next ()) {
                result.add (new User (resultSet.getString ("name"),
                        resultSet.getString ("lastName"),
                        resultSet.getByte ("age")));
            }
        } catch ( SQLException e ) {
            System.out.println ("Не удалось вывести юзеров");
        }

        return result;
    }

    @Override
    public void cleanUsersTable () {
        // Очистка содержания таблицы
        try (Statement statement = connection.createStatement ()) {
            statement.executeUpdate ("TRUNCATE TABLE users");
        } catch ( SQLException e ) {
            e.printStackTrace ();
        }
    }
}