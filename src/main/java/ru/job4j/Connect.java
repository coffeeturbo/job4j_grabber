package ru.job4j;

import java.sql.*;
import java.util.Properties;

public class Connect {

    private Connection connection;


    public Connect(Properties config) {
        init(config);
    }

    public Connection getConnection() {
        return this.connection;
    }


    public void init(Properties config) {
        try {
            Class.forName(config.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                config.getProperty("jdbc.url"),
                config.getProperty("jdbc.username"),
                config.getProperty("jdbc.password")
            );
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }


    public void insert(long date) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO rabbit(created_at) VALUES(?)",
            Statement.RETURN_GENERATED_KEYS)) {
            statement.setDate(1, new Date(date));
            statement.executeUpdate();

            ResultSet rsl = statement.getGeneratedKeys();
            rsl.next();
            rsl.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
