package com.xong.DB;

import java.sql.*;
import java.util.ArrayList;

public class StatesDB {

    private static String tableName = "states";
    private static String stateColumn = "state";
    private static String statusColumn = "status";

    public StatesDB() {
        try {

            String Driver = "org.sqlite.JDBC";
            Class.forName(Driver);

        } catch (ClassNotFoundException cnfe) {
            System.out.println("No database drivers found.");
            System.out.println(cnfe);
            System.exit(-1);
        }
    }

    public static ArrayList<String> getStatesList() {

        ArrayList<String> statesList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(DBConfig.db_url)) {

            String getAllData = "SELECT * FROM " + tableName + " WHERE " + statusColumn + " = 1";
            PreparedStatement ps = connection.prepareStatement(getAllData);
            ResultSet resultSet = ps.executeQuery();

            if(resultSet != null) {
                while(resultSet.next()) {
                    String state = resultSet.getString("state");

                    statesList.add(state);
                }
            }

            ps.close();
            connection.close();

        } catch (SQLException se) {
            se.printStackTrace();
            System.exit(-1);
        }

        return statesList;

    }

    public static int startOver() {

        int updated = -1;

        try(Connection connection = DriverManager.getConnection(DBConfig.db_url)) {

            String update = "UPDATE " + tableName + " SET " + statusColumn + " = ? WHERE " + statusColumn + " = ?";
            PreparedStatement ps = connection.prepareStatement(update);

            ps.setInt(1, 1);
            ps.setInt(2, 2);

            updated = ps.executeUpdate();

            ps.close();
            connection.close();

        } catch (SQLException se) {
            se.printStackTrace();
            System.exit(-1);
        }

        return updated;

    }

    public static boolean changeStateStatus(String userText) {

        boolean updated = false;

        try(Connection connection = DriverManager.getConnection(DBConfig.db_url)) {

            String update = "UPDATE " + tableName + " SET " + statusColumn + " = ? WHERE " + stateColumn + " = ?";
            PreparedStatement ps = connection.prepareStatement(update);

            ps.setInt(1, 2);
            ps.setString(2, userText);

            int i = ps.executeUpdate();

            updated = i > 0;

            ps.close();
            connection.close();

        } catch (SQLException se) {
            se.printStackTrace();
            System.exit(-1);
        }

        return updated;
    }

    public static int getScore() {

        int numOfCorrectStates = 0;

        try(Connection connection = DriverManager.getConnection(DBConfig.db_url)) {

            String getCorrectStates = "SELECT count(*)" + " FROM " + tableName + " WHERE " + statusColumn + " = ?";
            PreparedStatement ps = connection.prepareStatement(getCorrectStates);

            ps.setInt(1, 2);

            ResultSet resultSet = ps.executeQuery();

            resultSet.next();

            numOfCorrectStates = resultSet.getInt(1);

            ps.close();
            connection.close();

        } catch (SQLException se) {
            se.printStackTrace();
            System.exit(-1);
        }

        return numOfCorrectStates;
    }
}
