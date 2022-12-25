package de.lunarakai.lunaauction.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQueries {

    public static ResultSet equalsQuery(String _select, String _fromTable, String _condition, String _equals) throws SQLException {
        String sqlQueryCheck = "select " + _select + " from " + _fromTable + " where " + _condition + " = ?";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(sqlQueryCheck);
        preparedStatement.setString(1, String.valueOf(_equals));
        return preparedStatement.executeQuery();
    }
}
