package it.unipi.iot.userControl;

import it.unipi.iot.coap_client_handler.MyCoapClient;
import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class UserInputController implements Runnable{

    private final static MyCoapClient coapClient = MyCoapClient.getInstance();

    public void showCommandList(){
        System.out.println("****************************************************************");
        System.out.println("                              Commands                          ");
        System.out.println("****************************************************************");
        System.out.println("[1] view area");
        System.out.println("[2] view configurations");
        System.out.println("[3] view last temperature per area");
        System.out.println("[4] view last humidity per area");
        System.out.println("[5] view last presence per area");
        System.out.println("[6] add area");
        System.out.println("[7] update area max presence");
        System.out.println("\n[8] get current temperature");
        System.out.println("[9] get current humidity");
        System.out.println("[10] get current presence");
        System.out.println("[11] turn on air conditioner");
        System.out.println("[12] turn off air conditioner");
        System.out.println("[13] turn on dehumidifier");
        System.out.println("[14] turn off dehumidifier");
        System.out.println("[15] turn on semaphore");
        System.out.println("[16] turn off semaphore");
        System.out.println("\n[17] exit");
        System.out.println("****************************************************************");
    }

    public void selectCommand(){
        boolean loop = true;
        while(loop){
            showCommandList();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                int command = Integer.parseInt(reader.readLine());

                switch (command) {
                    case 1 -> { // view area
                        ResultSet result = FitSenseDBHandler.getAreas();
                        if (result != null) {
                            while (result.next()) {
                                System.out.println("id: " + result.getInt("id") +
                                        "| name: " + result.getString("name") +
                                        "| max_presence :" + result.getInt("max_presence"));

                            }
                        } else {
                            System.out.println("There are no areas");
                        }
                    }
                    case 2 -> { // view configurations
                        ResultSet result = FitSenseDBHandler.getConfigurations();
                        if (result != null) {
                            while (result.next()) {
                                System.out.println("area_id: " + result.getInt("area_id") +
                                        "| node_id: " + result.getInt("node_id") +
                                        "| address: " + result.getString("address"));

                            }
                        } else {
                            System.out.println("There are no actuators");
                        }
                    }
                    case 3 -> { // view last temperature per area
                        Map<Integer, Integer> lastTemperature = FitSenseDBHandler.getLastTemperature();
                        for (Map.Entry<Integer, Integer> entry : lastTemperature.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                    }
                    case 4 -> {// view last humidity per area
                        Map<Integer, Integer> lastHumidity = FitSenseDBHandler.getLastHumidity();
                        for (Map.Entry<Integer, Integer> entry : lastHumidity.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                    }
                    case 5 -> { // view last presence per area
                        Map<Integer, Integer> lastPresence = FitSenseDBHandler.getLastPresence();
                        for (Map.Entry<Integer, Integer> entry : lastPresence.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                    }
                    case 6 -> { // add area
                        System.out.println("Insert name area:");
                        String name = reader.readLine();
                        System.out.println("Insert max_presence area:");
                        int max_presence = Integer.parseInt(reader.readLine());
                        FitSenseDBHandler.insertArea(name, max_presence);
                    }
                    case 7 -> { // update area max presence
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            System.out.println("Insert new max_presence area:");
                            int max_presence = Integer.parseInt(reader.readLine());
                            FitSenseDBHandler.updateMaxPresenceArea(area_id, max_presence);
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 8 -> { // get current temperature
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                int value = coapClient.getRequestTemperature(addresses.get(0));
                                System.out.println("Temperature: " + value);
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 9 -> { // get current humidity
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                int value = coapClient.getRequestHumidity(addresses.get(0));
                                System.out.println("Humidity: " + value);
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 10 -> {  // get current presence
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                int value = coapClient.getRequestPresence(addresses.get(0));
                                System.out.println("Presence: " + value);
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 11 -> { // turn on air conditioner
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"on\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToAirConditioner(address);
                                    if (status.equals("off"))
                                        coapClient.putCommandToAirConditioner(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 12 -> {// turn off air-conditioner
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"off\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToAirConditioner(address);
                                    if (status.equals("on"))
                                        coapClient.putCommandToAirConditioner(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 13 -> { // turn on dehumidifier
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"on\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToDehumidifier(address);
                                    if (status.equals("off"))
                                        coapClient.putCommandToDehumidifier(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 14 -> { // turn off dehumidifier
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"off\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToDehumidifier(address);
                                    if (status.equals("on"))
                                        coapClient.putCommandToDehumidifier(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 15 -> { // turn on semaphore
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"on\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToSemaphore(address);
                                    if (status.equals("off"))
                                        coapClient.putCommandToSemaphore(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 16 -> { // turn off semaphore
                        System.out.println("Insert area id:");
                        int area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            ArrayList<String> addresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
                            if (addresses.isEmpty()) {
                                System.out.println("There are no actuators in this area");
                            } else {
                                String msg = "{\"mode\": \"off\"}";
                                for (String address : addresses) {
                                    String status = coapClient.getRequestToSemaphore(address);
                                    if (status.equals("on"))
                                        coapClient.putCommandToSemaphore(address, msg);
                                }
                            }
                        } else {
                            System.out.println("There is no area with this id");
                        }
                    }
                    case 17 -> //exit
                            loop = false;
                    default -> System.out.println("You must insert an integer between 1 and 17");
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("You must insert an integer");
            } catch (SQLException | ConnectorException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        selectCommand();
    }
}
