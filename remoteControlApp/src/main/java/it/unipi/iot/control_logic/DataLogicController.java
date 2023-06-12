package it.unipi.iot.control_logic;

import it.unipi.iot.coap_client_handler.MyCoapClient;
import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DataLogicController implements Runnable {
    private final static MyCoapClient coapClient = MyCoapClient.getInstance();
    public static void temperatureController() throws ConnectorException, IOException, ParseException {
        Map<Integer, Integer> valuePerArea = FitSenseDBHandler.getLastTemperature();

        for (Map.Entry<Integer, Integer> entry : valuePerArea.entrySet()) {
            int area_id = entry.getKey();
            int value = entry.getValue();
            String command;
            ArrayList<String> actuatorAddresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
            for(String address : actuatorAddresses){
                String status = coapClient.getRequestToAirConditioner(address);
                if((value > 24 || value < 19) && !status.equals("on")){
                    // send on message to air conditioner
                    command = "{\"mode\": \"on\"}";
                    System.out.println("[!] Sending PUT request to air conditioner " + address);
                    coapClient.putCommandToAirConditioner(address, command);
                } else if( (value <= 24 || value >= 19) && !status.equals("off")){
                    // send off message to air conditioner
                    command = "{\"mode\": \"off\"}";
                    System.out.println("[!] Sending PUT request to air conditioner " + address);
                    coapClient.putCommandToAirConditioner(address, command);
                }
            }
        }
    }

    public static void humidityController() throws ConnectorException, IOException, ParseException {
        Map<Integer, Integer> valuePerArea = FitSenseDBHandler.getLastHumidity();

        for (Map.Entry<Integer, Integer> entry : valuePerArea.entrySet()) {
            int area_id = entry.getKey();
            int value = entry.getValue();
            String command;
            ArrayList<String> actuatorAddresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
            for(String address : actuatorAddresses){
                String status = coapClient.getRequestToDehumidifier(address);
                if((value > 25 || value < 20) && !status.equals("on")){
                    // send on message to dehumidifier
                    command = "{\"mode\": \"on\"}";
                    System.out.println("[!] Sending PUT request to dehumidifier " + address);
                    coapClient.putCommandToDehumidifier(address, command);
                } else if( (value <= 25 || value >= 20) && !status.equals("off")){
                    // send off message to dehumidifier
                    command = "{\"mode\": \"off\"}";
                    System.out.println("[!] Sending PUT request to dehumidifier " + address);
                    coapClient.putCommandToDehumidifier(address, command);
                }
            }
        }
    }

    public static void presenceController() throws ConnectorException, IOException, ParseException {
        Map<Integer, Integer> valuePerArea = FitSenseDBHandler.getLastPresence();

        for (Map.Entry<Integer, Integer> entry : valuePerArea.entrySet()) {
            int area_id = entry.getKey();
            int value = entry.getValue();
            String command;
            ArrayList<String> actuatorAddresses = FitSenseDBHandler.getActuatorFromAreaID(area_id);
            for(String address : actuatorAddresses){
                String status = coapClient.getRequestToSemaphore(address);
                int maxPresence = FitSenseDBHandler.getMaxPresenceArea(area_id);
                if(value > maxPresence && !status.equals("on")){
                    // send on message to semaphore
                    command = "{\"mode\": \"on\"}";
                    System.out.println("[!] Sending PUT request to semaphore " + address);
                    coapClient.putCommandToSemaphore(address, command);
                } else if( value <= maxPresence && !status.equals("off")){
                    // send off message to semaphore
                    command = "{\"mode\": \"off\"}";
                    System.out.println("[!] Sending PUT request to semaphore " + address);
                    coapClient.putCommandToSemaphore(address, command);
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            temperatureController();
            humidityController();
            presenceController();
        } catch (ConnectorException | IOException | ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
