package de.lunarakai.lunaauction.sql;

import de.lunarakai.lunaauction.LunaAuction;
import org.bukkit.persistence.PersistentDataContainer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Database {

    static LunaAuction plugin = LunaAuction.getPlugin();
    static DatabaseQueries queries = new DatabaseQueries();
    static DatabaseInsertion insertion = new DatabaseInsertion();
    static Connection connection;

    public static void connect() throws SQLException, IOException {

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

    public static boolean isConnected() {
        return connection != null;
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer getIdFromTable(UUID uuid) throws SQLException {
        ResultSet result = queries.equalsQuery("ID", "players", "UUID", String.valueOf(uuid));
        result.next();
        return ((Number) result.getObject(1)).intValue();
    }

    public static void insertPlayerData(String table, UUID sqlUuid, String sqlPlayerName)  {
        try {
            insertion.insertStringData(table, "UUID", "Name", String.valueOf(sqlUuid), sqlPlayerName);
        } catch (Exception e) {
            LunaAuction.LOGGER.info(String.valueOf(e));
        }
    }

    //TODO
    public static void insertAuctionData(String table, Integer playerID, String itemStack, PersistentDataContainer item, Integer currentPrice) {
        try {
            insertion.insertData(table, "playerID", "item", "currentPrice", "itemStack",
                    playerID, item, currentPrice, itemStack);
        } catch (Exception e) {
            LunaAuction.LOGGER.info(String.valueOf(e));
        }
    }
}
