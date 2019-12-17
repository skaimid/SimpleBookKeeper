package cn.skaimid.SimpleBookKeeper.util;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class SqlUtil {

    public static void init(){
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("create table if not exists account " +
                    "(id integer primary key autoincrement not null, " +
                    "money real default 0, " +
                    "tag integer default 0," +
                    "description text, " +
                    "time text, " +
                    "last_update_time text);");

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


    /**
     * for xlsx export function
     * @return account data as queue
     */
    public static Queue<Account> handleTraversing() {
        Queue<Account> accountQueue = new LinkedList<>();

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:account.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("select * from account order by time desc");
            while (rs.next()) {
                accountQueue.offer(new Account(
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

        return accountQueue;
    }

    /**
     * @param sql sql expression
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
                    ",'" + account.getDescription() + "','" + SqlTimeUtil.format(account.getDate()) + "',DATETIME('now'))");


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

