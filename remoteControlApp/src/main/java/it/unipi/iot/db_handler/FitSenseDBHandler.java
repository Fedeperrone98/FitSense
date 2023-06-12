package it.unipi.iot.db_handler;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FitSenseDBHandler {
    @SuppressWarnings("finally")
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

    public static ArrayList<String> getActuatorFromAreaID(int area_id){
        ArrayList<String> result = new ArrayList<>();
        String selectQueryStatement = "SELECT * FROM configuration WHERE area_id=?";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();
                while (resultSet.next()){
                    result.add(resultSet.getString("address"));
                }

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
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

    public static void insertPresence(int area_id, int node_id, int m_value) {
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

    public static void insertConfiguration(int area_id, int node_id, String address) {
        String insertQueryStatement = "INSERT INTO measurement_temperature (area_id, node_id, address) VALUES (?, ?, ?)";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                smartPoolPrepareStat.setInt(2, node_id);
                smartPoolPrepareStat.setString(3, address);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static void insertArea( String name, int max_presence) {
        String insertQueryStatement = "INSERT INTO area (name_area, max_presence) VALUES (?, ?)";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement)
            ) {
                smartPoolPrepareStat.setString(1, name);
                smartPoolPrepareStat.setInt(2, max_presence);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static void updateMaxPresenceArea (int area_id, int max_presence){
        String updateQueryStatement = "UPDATE area SET max_presence = ? WHERE area_id = ?";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(updateQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, max_presence);
                smartPoolPrepareStat.setInt(2, area_id);
                smartPoolPrepareStat.executeUpdate();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public static boolean checkAreaExistence(int id){
        String selectQueryStatement = "SELECT * FROM area WHERE id=?";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, id);
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                return resultSet.next();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return false;
    }

    public static ResultSet getAreas(){
        String selectQueryStatement = "SELECT * FROM area";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                // Execute the query
                return smartPoolPrepareStat.executeQuery();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    public static ResultSet getConfigurations(){
        String selectQueryStatement = "SELECT * FROM configuration";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                // Execute the query
                return smartPoolPrepareStat.executeQuery();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return null;
    }

    public static int getMaxPresenceArea(int id){
        String selectQueryStatement = "SELECT * FROM area WHERE id=?";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, id);
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                return resultSet.getInt("max_presence");

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return 0;
    }

    public static boolean checkNodeExistence(int area_id, int node_id){
        String selectQueryStatement = "SELECT * FROM configuration WHERE area_id=? and node_id=?";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement)
            ) {
                smartPoolPrepareStat.setInt(1, area_id);
                smartPoolPrepareStat.setInt(2, node_id);
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                return resultSet.next();

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return false;
    }

    public static Map<Integer, Integer> getLastTemperature(){
        Map<Integer, Integer> result = new HashMap<>();

        String selectQueryStatement1 = "SELECT area_id, MAX(m_timestamp) as maxTime FROM measurement_temperature GROUP BY area_id";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement1)
            ) {
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                while (resultSet.next()){
                    int currentAreaId = resultSet.getInt("area_id");
                    Timestamp maxTime = resultSet.getTimestamp("maxTime");
                    String selectQueryStatement2 = "SELECT m_value FROM measurement_temperature WHERE area_id=? and m_timestamp=?";
                    try (Connection smartPoolConnection2 = makeDBConnection()
                    ) {
                        assert smartPoolConnection2 != null;
                        try (PreparedStatement smartPoolPrepareStat2 = smartPoolConnection2.prepareStatement(selectQueryStatement2)
                        ) {
                            smartPoolPrepareStat2.setInt(1, currentAreaId);
                            smartPoolPrepareStat2.setTimestamp(2, maxTime);
                            // Execute the query
                            ResultSet resultSet2 = smartPoolPrepareStat2.executeQuery();
                            result.put(currentAreaId, resultSet2.getInt("m_value"));
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }
                }

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
    }

    public static Map<Integer, Integer> getLastHumidity(){
        Map<Integer, Integer> result = new HashMap<>();

        String selectQueryStatement1 = "SELECT area_id, MAX(m_timestamp) as maxTime FROM measurement_humidity GROUP BY area_id";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement1)
            ) {
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                while (resultSet.next()){
                    int currentAreaId = resultSet.getInt("area_id");
                    Timestamp maxTime = resultSet.getTimestamp("maxTime");
                    String selectQueryStatement2 = "SELECT m_value FROM measurement_humidity WHERE area_id=? and m_timestamp=?";
                    try (Connection smartPoolConnection2 = makeDBConnection()
                    ) {
                        assert smartPoolConnection2 != null;
                        try (PreparedStatement smartPoolPrepareStat2 = smartPoolConnection2.prepareStatement(selectQueryStatement2)
                        ) {
                            smartPoolPrepareStat2.setInt(1, currentAreaId);
                            smartPoolPrepareStat2.setTimestamp(2, maxTime);
                            // Execute the query
                            ResultSet resultSet2 = smartPoolPrepareStat2.executeQuery();
                            result.put(currentAreaId, resultSet2.getInt("m_value"));
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }
                }

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
    }

    public static Map<Integer, Integer> getLastPresence(){
        Map<Integer, Integer> result = new HashMap<>();

        String selectQueryStatement1 = "SELECT area_id, MAX(m_timestamp) as maxTime FROM measurement_presence GROUP BY area_id";
        try (Connection smartPoolConnection = makeDBConnection()
        ) {
            assert smartPoolConnection != null;
            try (PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(selectQueryStatement1)
            ) {
                // Execute the query
                ResultSet resultSet = smartPoolPrepareStat.executeQuery();

                while (resultSet.next()){
                    int currentAreaId = resultSet.getInt("area_id");
                    Timestamp maxTime = resultSet.getTimestamp("maxTime");
                    String selectQueryStatement2 = "SELECT m_value FROM measurement_presence WHERE area_id=? and m_timestamp=?";
                    try (Connection smartPoolConnection2 = makeDBConnection()
                    ) {
                        assert smartPoolConnection2 != null;
                        try (PreparedStatement smartPoolPrepareStat2 = smartPoolConnection2.prepareStatement(selectQueryStatement2)
                        ) {
                            smartPoolPrepareStat2.setInt(1, currentAreaId);
                            smartPoolPrepareStat2.setTimestamp(2, maxTime);
                            // Execute the query
                            ResultSet resultSet2 = smartPoolPrepareStat2.executeQuery();
                            result.put(currentAreaId, resultSet2.getInt("m_value"));
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }
                }

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        return result;
    }
}
