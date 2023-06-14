package it.unipi.iot.userControl;

import it.unipi.iot.coap_client_handler.MyCoapClient;
import it.unipi.iot.db_handler.FitSenseDBHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.ResultSet;
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

                int area_id;
                int max_presence;

                switch (command) {
                    case 1:  // view area
                        ArrayList<ArrayList<String>> areas = FitSenseDBHandler.getAreas();
                        assert areas != null;
                        if (!areas.isEmpty()) {
                            for(ArrayList<String> area : areas){
                                System.out.println("id: " + area.get(0) +
                                        "\t| name: " + area.get(1) +
                                        "\t| max_presence: " + area.get(2));
                            }
                        } else {
                            System.out.println("There are no areas");
                        }
                        break;

                    case 2: // view configurations
                        ArrayList<ArrayList<String>> nodes = FitSenseDBHandler.getConfigurations();
                        assert nodes != null;
                        if (!nodes.isEmpty()) {
                            for(ArrayList<String> node : nodes){
                                System.out.println("area_id: " + node.get(0) +
                                        "\t| node_id: " + node.get(1) +
                                        "\t| address: " + node.get(2));
                            }
                        } else {
                            System.out.println("There are no actuators");
                        }
                        break;

                    case 3 : // view last temperature per area
                        Map<Integer, Integer> lastTemperature = FitSenseDBHandler.getLastTemperature();
                        for (Map.Entry<Integer, Integer> entry : lastTemperature.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                        break;

                    case 4: // view last humidity per area
                        Map<Integer, Integer> lastHumidity = FitSenseDBHandler.getLastHumidity();
                        for (Map.Entry<Integer, Integer> entry : lastHumidity.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                        break;
                    case 5:  // view last presence per area
                        Map<Integer, Integer> lastPresence = FitSenseDBHandler.getLastPresence();
                        for (Map.Entry<Integer, Integer> entry : lastPresence.entrySet()) {
                            System.out.println("area_id: " + entry.getKey() +
                                    "| value: " + entry.getValue());
                        }
                        break;

                    case 6:  // add area
                        System.out.println("Insert name area:");
                        String name = reader.readLine();
                        System.out.println("Insert max_presence area:");
                        max_presence = Integer.parseInt(reader.readLine());
                        FitSenseDBHandler.insertArea(name, max_presence);
                        break;

                    case 7:  // update area max presence
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
                        if (FitSenseDBHandler.checkAreaExistence(area_id)) {
                            System.out.println("Insert new max_presence area:");
                            max_presence = Integer.parseInt(reader.readLine());
                            FitSenseDBHandler.updateMaxPresenceArea(area_id, max_presence);
                        } else {
                            System.out.println("There is no area with this id");
                        }
                        break;

                    case 8:  // get current temperature
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 9:  // get current humidity
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 10:   // get current presence
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 11:  // turn on air conditioner
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 12: // turn off air-conditioner
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 13:  // turn on dehumidifier
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 14:  // turn off dehumidifier
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 15:  // turn on semaphore
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 16:  // turn off semaphore
                        System.out.println("Insert area id:");
                        area_id = Integer.parseInt(reader.readLine());
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
                        break;

                    case 17: //exit
                            loop = false;
                            System.exit(0);
                            break;
                    default:
                        System.out.println("You must insert an integer between 1 and 17");
                        break;
                }

            } catch(IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
	}
    }

    @Override
    public void run() {
        selectCommand();
    }
}
