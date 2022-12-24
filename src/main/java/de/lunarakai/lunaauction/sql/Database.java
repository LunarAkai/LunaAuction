package de.lunarakai.lunaauction.sql;

import de.lunarakai.lunaauction.LunaAuction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    LunaAuction plugin = LunaAuction.getPlugin();

    private Connection connection;

    public void connect() throws SQLException, IOException {

        String host = plugin.getConfig().getString("database_config.host");
        int port = plugin.getConfig().getInt("database_config.port");
        String database = plugin.getConfig().getString("database_config.database");
        String username = plugin.getConfig().getString("database_config.username");
        String password = plugin.getConfig().getString("database_config.password");

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false",
                username,
                password
        );
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
