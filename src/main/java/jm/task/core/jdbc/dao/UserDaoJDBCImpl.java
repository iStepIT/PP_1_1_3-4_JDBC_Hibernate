package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {}
    @Override
    public void createUsersTable() {
// Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users(id bigint AUTO_INCREMENT PRIMARY KEY, name varchar(100) NOT NULL, lastName varchar(100) NOT NULL, age tinyint DEFAULT 0)";
            statement.executeUpdate(sql);
            System.out.println("Создали таблицу");

        } catch (SQLException e) {
            System.out.println("Не удалось создать таблицу");
        }
    }
    @Override
    public void dropUsersTable() {
        //Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()){
            String sql = "DROP TABLE IF EXISTS Users";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Пользователи не удалились");
        }
    }
    @Override
    public void saveUser(String name, String lastName, byte age) {
        // Добавление User в таблицу
        String sql = "INSERT INTO Users ( name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Пользователи не сохранились");
        }
    }
    @Override
    public void removeUserById(long id) {
        //Удаление User из таблицы ( по id )
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Пользователи не сохранились");
        }
    }
    @Override
    public List<User> getAllUsers() {
        //Получение всех User(ов) из таблицы
        List<User>  result = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                result.add(new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")));
            }
        } catch (SQLException e) {
            System.out.println("Не удалось вывести юзеров");
        }
        return result;
    }
    @Override
    public void cleanUsersTable() {
        // Очистка содержания таблицы
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate("TRUNCATE TABLE Users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}