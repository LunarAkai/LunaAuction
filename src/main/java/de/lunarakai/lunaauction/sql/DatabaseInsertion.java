package de.lunarakai.lunaauction.sql;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsertion {



    public void insertData(String intoTable, String column1, String column2, String column3, String column4,
                           Integer id, PersistentDataContainer persistentDataContainer,
                           Integer price, ItemStack itemStack) throws SQLException {
        String sql = "insert into " + intoTable + " (" + column1 + ", " + column2 + ", " + column3 + ", " + column4+ ") "+ " values (?,?,?,?)";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, String.valueOf(persistentDataContainer));
        preparedStatement.setInt(3, price);
        preparedStatement.setString(4, String.valueOf(itemStack));
        preparedStatement.execute();
    }

    public void insertStringData(String intoTable, String column1, String column2, String stringValue1, String stringValue2) throws SQLException {
        String sql = "insert into " + intoTable + " (" + column1 + ", " + column2 + ") " + "values (?,?)";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(stringValue1));
        preparedStatement.setString(2, String.valueOf(stringValue2));
        preparedStatement.execute();
    }
}