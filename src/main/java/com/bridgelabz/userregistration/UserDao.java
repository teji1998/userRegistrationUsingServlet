package com.bridgelabz.userregistration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private String jdbcURL = "jdbc:mysql://localhost:3306/world?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "teju1998";

    private static final String INSERT_USERS_SQL = "INSERT INTO user" + "  (firstName, lastName, email, password, phoneNumber) VALUES "
            + " (?, ?, ?,?,?);";

    private static final String SELECT_USER_BY_ID = "select id, firstName, lastName, email, password, phoneNumber from user where id =?";
    private static final String SELECT_ALL_USERS = "select * from user";
    private static final String DELETE_USERS_SQL = "delete from user where id = ?;";
    private static final String UPDATE_USERS_SQL = "update user set firstName = ?, lastName = ?, email = ?, password = ?, phoneNumber = ? where id = ?;";

    public UserDao() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertUser(User user) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhoneNumber());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phoneNumber");
                user = new User(id, firstName,lastName,email,password,phoneNumber);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phoneNumber = rs.getString("phoneNumber");
                users.add(new User(id, firstName, lastName, email, password, phoneNumber));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean deleteUser(int id) throws SQLException {
        boolean deleteUser;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            deleteUser = statement.executeUpdate() > 0;
        }
        return deleteUser;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean updateUser;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getPhoneNumber());
            statement.setInt(6, user.getId());

            updateUser = statement.executeUpdate() > 0;
        }
        return updateUser;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}

