package cn.skaimid.SimpleBookKeeper.util;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.jvm.hotspot.debugger.Address;

import java.sql.*;

public class SqlUtil {
    /**
     * @param sql sql
     * @return ObservableList<Account> for column
     */
    public static ObservableList<Account> handleSearch(String sql) {
        ObservableList<Account> accountData = FXCollections.observableArrayList();
        // connect to the database
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                accountData.add(new Account(
                        rs.getInt("id"),
                        rs.getDouble("money"),
                        SqlTimeUtil.parse(rs.getString("time")),
                        rs.getInt("tag"),
                        rs.getString("description")));

            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return accountData;
    }

    public static void handleDelete(int id) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("delete from account where id = ' " + id + " '");

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    public static void handleAdd(Account account) {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("insert into account(id,money, tag, description, time, last_update_time)" +
                    "values (null,'" + account.getMoney() + "','" + Tags.getCodeByTagName(account.getTag()) + "'" +
                    ",'" + account.getDescription() + "','" + SqlTimeUtil.formate(account.getDate()) + "',DATETIME('now'))");


        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            System.err.println("1!");
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());

            }
        }
    }

    public static int getLastId() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select max(id) from account;");
            rs.next();
            return rs.getInt(1);


        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }


}

