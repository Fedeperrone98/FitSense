package it.unipi.iot.coap_client_handler;

import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class MyCoapClient extends CoapClient {

    private static MyCoapClient instance = null;
    private CoapClient clientAirConditioner = null;
    private CoapClient clientDehumidifier = null;
    private CoapClient clientSemaphore = null;

    public static MyCoapClient getInstance() {
        if (instance == null)
            instance = new MyCoapClient();
        return instance;
    }

    public int getRequestTemperature(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient temperature = new CoapClient("coap://[" + ipAddress + "]/actuator/temperature");

        // Send the get request to temperature sensor
        System.out.println("[+] GET request to temperature sensor");
        CoapResponse temperatureResponse = temperature.get();
        String responseText = temperatureResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (int) genreJsonObject.get("value");
    }

    public int getRequestHumidity(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient humidity = new CoapClient("coap://[" + ipAddress + "]/actuator/humidity");

        // Send the get request to the humidity sensor
        System.out.println("[+] GET request to humidity sensor");
        CoapResponse humidityResponse = humidity.get();
        String responseText = humidityResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (int) genreJsonObject.get("value");
    }

    public int getRequestPresence(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient presence = new CoapClient("coap://[" + ipAddress + "]/actuator/presence");

        // Send the get request to the presence sensor
        System.out.println("[+] GET request to presence sensor");
        CoapResponse presenceResponse = presence.get();
        String responseText = presenceResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (int) genreJsonObject.get("value");
    }

    public String getRequestToAirConditioner(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");

        // Send the get request to the air conditioner
        System.out.println("[+] GET request to air conditioner");
        CoapResponse airConditionerResponse = clientAirConditioner.get();
        String responseText = airConditionerResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (String) genreJsonObject.get("value");
    }

    public void putCommandToAirConditioner(String ipAddress, String command) throws ConnectorException, IOException {
        CoapClient clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");

        // Send the PUT request to handle the air conditioner
        CoapResponse airConditionerResponse = clientAirConditioner.put(command, MediaTypeRegistry.TEXT_PLAIN);
        System.out.println(" > " + command);

        // Check the response
        if (airConditionerResponse.isSuccess()) {
            System.out.println("[+] PUT request succeeded");
        } else {
            System.out.println("[-] PUT request failed");
        }
    }

    public String getRequestToDehumidifier(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");

        // Send the get request to the dehumidifier
        System.out.println("[+] GET request to dehumidifier");
        CoapResponse airConditionerResponse = clientDehumidifier.get();
        String responseText = airConditionerResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (String) genreJsonObject.get("value");
    }

    public void putCommandToDehumidifier(String ipAddress, String command) throws ConnectorException, IOException {
        CoapClient clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");

        // Send the PUT request to handle the dehumidifier
        CoapResponse airConditionerResponse = clientDehumidifier.put(command, MediaTypeRegistry.TEXT_PLAIN);
        System.out.println(" > " + command);

        // Check the response
        if (airConditionerResponse.isSuccess()) {
            System.out.println("[+] PUT request succeeded");
        } else {
            System.out.println("[-] PUT request failed");
        }
    }

    public String getRequestToSemaphore(String ipAddress) throws ConnectorException, IOException, ParseException {
        CoapClient clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");

        // Send the get request to the semaphore
        System.out.println("[+] GET request to semaphore");
        CoapResponse airConditionerResponse = clientSemaphore.get();
        String responseText = airConditionerResponse.getResponseText();
        System.out.println(" < " + responseText);

        JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
        return (String) genreJsonObject.get("value");
    }

    public void putCommandToSemaphore(String ipAddress, String command) throws ConnectorException, IOException {
        CoapClient clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");

        // Send the PUT request to handle the semaphore
        CoapResponse airConditionerResponse = clientSemaphore.put(command, MediaTypeRegistry.TEXT_PLAIN);
        System.out.println(" > " + command);

        // Check the response
        if (airConditionerResponse.isSuccess()) {
            System.out.println("[+] PUT request succeeded");
        } else {
            System.out.println("[-] PUT request failed");
        }
    }

    public void startTemperatureObservation(String ipAddress) {
        CoapClient clientTemperatureObs = new CoapClient("coap://[" + ipAddress + "]/actuator/temperature");
        clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");
        clientTemperatureObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handleTemperatureResponse(response, ipAddress);
                    }

                    public void onError() {
                        System.out.println("[-] Observing temperature failed");
                    }
                }
        );
    }

    public void handleTemperatureResponse(CoapResponse response, String address){
        System.out.println("[!] Receiving temperature notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToAirConditioner(address);
            if ((value > 24 || value < 19) && !status.equals("on")){
                // send on message to air conditioner
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the air conditioner
                CoapResponse airConditionerResponse = clientAirConditioner.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (airConditionerResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            } else if( (value <= 24 || value >= 19) && !status.equals("off")){
                // send off message to air conditioner
                jsonString_response = "{\"mode\": \"off\"}";

                // Send the PUT request to handle the air conditioner
                CoapResponse airConditionerResponse = clientAirConditioner.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (airConditionerResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }
        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startHumidityObservation(String ipAddress) {
        CoapClient clientHumidityObs = new CoapClient("coap://[" + ipAddress + "]/actuator/humidity");
        clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");
        clientHumidityObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handleHumidityResponse(response, ipAddress);
                    }

                    public void onError() {
                        System.out.println("[-] Observing humidity failed");
                    }
                }
        );
    }

    public void handleHumidityResponse(CoapResponse response, String address) {
        System.out.println("[!] Receiving humidity notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToDehumidifier(address);
            if ((value > 25 || value < 20) && !status.equals("on")){
                // send on message to dehumidifier
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the dehumidifier
                CoapResponse dehumidifierResponse = clientDehumidifier.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (dehumidifierResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            } else if( (value <= 25 || value >= 20) && !status.equals("off")){
                // send off message to dehumidifier
                jsonString_response = "{\"mode\": \"off\"}";

                // Send the PUT request to handle the dehumidifier
                CoapResponse dehumidifierResponse = clientDehumidifier.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (dehumidifierResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }

        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startPresenceObservation(String ipAddress, int area_id) {
        CoapClient clientPresenceObs = new CoapClient("coap://[" + ipAddress + "]/actuator/presence");
        clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");
        clientPresenceObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handlePresenceResponse(response, area_id, ipAddress);
                    }

                    public void onError() {
                        System.out.println("[-] Observing presence failed");
                    }
                }
        );
    }

    public void handlePresenceResponse(CoapResponse response, int area_id, String address) {
        System.out.println("[!] Receiving presence notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToSemaphore(address);
            int maxPresence = FitSenseDBHandler.getMaxPresenceArea(area_id);
            if (value > maxPresence && !status.equals("on")){
                // send on message to semaphore
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the semaphore
                CoapResponse semaphoreResponse = clientSemaphore.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (semaphoreResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            } else  if( value <= maxPresence && !status.equals("off")){
                // send off message to semaphore
                jsonString_response = "{\"mode\": \"off\"}";

                // Send the PUT request to handle the semaphore
                CoapResponse semaphoreResponse = clientSemaphore.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
                System.out.println(" > " + jsonString_response);

                // Check the response
                if (semaphoreResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }

        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
