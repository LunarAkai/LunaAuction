package de.lunarakai.lunaauction.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUpdate {

    public static void insertData(String intoTable, String setColumn, String whereColumn, Integer id, Integer integer) throws SQLException {
        String sql =
                "update " + intoTable + " set " + setColumn + " = ? where " + whereColumn + " = " + id;
        PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
        preparedStatement.setInt(1, integer);
        preparedStatement.executeUpdate();
    }
}
