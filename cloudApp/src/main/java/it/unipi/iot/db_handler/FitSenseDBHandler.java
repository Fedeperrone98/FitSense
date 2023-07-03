package it.unipi.iot.db_handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FitSenseDBHandler {

    private static Connection makeDBConnection() {
        Connection databaseConnection = null;

        String databaseIP = "localhost";
        String databasePort = "3306";
        String databaseUsername = "root";
        String databasePassword = "iotpassword";
        String databaseName = "iot_fitsense_db";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//checks if the Driver class exists (correctly available)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            databaseConnection = DriverManager.getConnection(
                    "jdbc:mysql://" + databaseIP + ":" + databasePort +
                            "/" + databaseName + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=CET",
                    databaseUsername,
                    databasePassword);
            //The Driver Manager provides the connection specified in the parameter string
            if (databaseConnection == null) {
                System.err.println("Connection to Db failed");
            }
        } catch (SQLException e) {
            System.err.println("MySQL Connection Failed!\n");
            e.printStackTrace();
        }finally {
            return databaseConnection;
        }
    }

    public static void insertTemperature(int area_id, int node_id, int m_value) {
        String insertQueryStatement = "INSERT INTO measurement_temperature (area_id, node_id, m_timestamp, m_value) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                smartPoolPrepareStat.setInt(2, node_id);
                smartPoolPrepareStat.setInt(3, m_value);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static void insertHumidity(int area_id, int node_id, int m_value) {
        String insertQueryStatement = "INSERT INTO measurement_humidity (area_id, node_id, m_timestamp, m_value) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                smartPoolPrepareStat.setInt(2, node_id);
                smartPoolPrepareStat.setInt(3, m_value);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static void insertPresence(int area_id, int node_id, int m_value) {
        String insertQueryStatement = "INSERT INTO measurement_presence (area_id, node_id, m_timestamp, m_value) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                smartPoolPrepareStat.setInt(2, node_id);
                smartPoolPrepareStat.setInt(3, m_value);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

    }

}
