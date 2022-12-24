package de.lunarakai.lunaauction.sql;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class Database {

    private Connection connection;

    public void connect() throws SQLException, IOException {

        Yaml yaml = new Yaml();
        InputStream inputStream = Files.newInputStream(Paths.get("../../../dbconfig.yml"));
        HashMap yamlMap = yaml.load(inputStream);

        HashMap yhost = (HashMap) yamlMap.get("HOST");
        HashMap yport = (HashMap) yamlMap.get("PORT");
        HashMap ydatabase = (HashMap) yamlMap.get("DATABASE");
        HashMap yusername = (HashMap) yamlMap.get("USERNAME");
        HashMap ypassword = (HashMap) yamlMap.get("PASSWORD");

        //TODO: hide values of DB connection
        String host = yhost.toString();
        int port = Integer.parseInt(yport.toString());
        String database = ydatabase.toString();
        String username = yusername.toString();
        String password = ypassword.toString();



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
