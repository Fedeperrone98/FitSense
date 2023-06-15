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

    private static CoapClient clientTemperatureObs = null;
    private static CoapClient clientAirConditioner = null;

    private static CoapClient clientHumidityObs = null;
    private static CoapClient clientDehumidifier = null;

    private static CoapClient clientPresenceObs = null;
    private static CoapClient clientSemaphore = null;

    public static MyCoapClient getInstance() {
        if (instance == null)
            instance = new MyCoapClient();
        return instance;
    }

    public int getRequestTemperature(String ipAddress) {
        CoapClient temperature = new CoapClient("coap://[" + ipAddress + "]/actuator/temperature");

        // Send the get request to temperature sensor
        System.out.println("[+] GET request to temperature sensor");

        try {
            CoapResponse temperatureResponse = temperature.get();
            String responseText = temperatureResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
            long value_parsed = (long) genreJsonObject.get("value");

            temperature.shutdown();

            return (int) value_parsed;
        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public int getRequestHumidity(String ipAddress) {
        CoapClient humidity = new CoapClient("coap://[" + ipAddress + "]/actuator/humidity");

        // Send the get request to the humidity sensor
        System.out.println("[+] GET request to humidity sensor");

        try {
            CoapResponse humidityResponse = humidity.get();
            String responseText = humidityResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
            long value_parsed = (long) genreJsonObject.get("value");

            humidity.shutdown();

            return (int) value_parsed;
        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public int getRequestPresence(String ipAddress) {
        CoapClient presence = new CoapClient("coap://[" + ipAddress + "]/actuator/presence");

        // Send the get request to the presence sensor
        System.out.println("[+] GET request to presence sensor");

        try {
            CoapResponse presenceResponse = presence.get();
            String responseText = presenceResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);
            long value_parsed = (long) genreJsonObject.get("value");

            presence.shutdown();

            return (int) value_parsed;
        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public String getRequestToAirConditioner(String ipAddress) {
        CoapClient clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");
        // Send the get request to the air conditioner
        System.out.println("[+] GET request to air conditioner");

        try {
            System.out.println("dentro il try");
            CoapResponse airConditionerResponse = clientAirConditioner.get();
            System.out.println("ho fatto la get");
            String responseText = airConditionerResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);

            clientAirConditioner.shutdown();

            return (String) genreJsonObject.get("value");
        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void putCommandToAirConditioner(String ipAddress, String command) {
        CoapClient clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");

        // Send the PUT request to handle the air conditioner
        try {
            CoapResponse airConditionerResponse = clientAirConditioner.put(command, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" >  " + command);

            // Check the response
            if (airConditionerResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }
            clientAirConditioner.shutdown();
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public String getRequestToDehumidifier(String ipAddress) {
        CoapClient clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");

        // Send the get request to the dehumidifier
        System.out.println("[+] GET request to dehumidifier");
        try {
            System.out.println("dentro il try");
            CoapResponse dehumidifierResponse = clientDehumidifier.get();
            System.out.println("ho fatto la get");
            String responseText = dehumidifierResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);

            clientDehumidifier.shutdown();

            return (String) genreJsonObject.get("value");

        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void putCommandToDehumidifier(String ipAddress, String command) {
        CoapClient clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");

        // Send the PUT request to handle the dehumidifier
        try {
            CoapResponse dehumidifierResponse = clientDehumidifier.put(command, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" >  " + command);

            // Check the response
            if (dehumidifierResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }

            clientDehumidifier.shutdown();
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public String getRequestToSemaphore(String ipAddress)  {
        CoapClient clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");

        // Send the get request to the semaphore
        System.out.println("[+] GET request to semaphore");
        try {
            System.out.println("dentro il try");
            CoapResponse semaphoreResponse = clientSemaphore.get();
            System.out.println("ho fatto la get");
            String responseText = semaphoreResponse.getResponseText();
            System.out.println(" <  " + responseText);

            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(responseText);

            clientSemaphore.shutdown();

            return (String) genreJsonObject.get("value");
        } catch (ConnectorException | IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void putCommandToSemaphore(String ipAddress, String command)  {
        CoapClient clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");

        // Send the PUT request to handle the semaphore
        try {
            CoapResponse semaphoreResponse = clientSemaphore.put(command, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" >  " + command);

            // Check the response
            if (semaphoreResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }
            clientSemaphore.shutdown();
        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void startTemperatureObservation(final String ipAddress) {
        clientTemperatureObs = new CoapClient("coap://[" + ipAddress + "]/actuator/temperature");
        clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");
        clientTemperatureObs.observe(
                new CoapHandler() {
                    @Override public void onLoad(CoapResponse response) {
                        handleTemperatureResponse(response, ipAddress);
                    }
                    @Override public void onError() {
                        System.out.println("[-] Observing temperature failed");
                    }
                }
        );
    }

    public void handleTemperatureResponse(CoapResponse response, String address){
        System.out.println("[!] Receiving temperature notification");
        String responseString = new String(response.getPayload());
        System.out.println(" <  " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            long value = (long) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToAirConditioner(address);
            if ((value > 24 || value < 19) && !status.equals("on")){
                // send on message to air conditioner
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the air conditioner
                System.out.println("[!] Sending PUT request to air conditioner");
                System.out.println(" >  " + jsonString_response);
                CoapResponse airConditionerResponse = clientAirConditioner.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

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
                System.out.println("[!] Sending PUT request to air conditioner");
                System.out.println(" >  " + jsonString_response);
                CoapResponse airConditionerResponse = clientAirConditioner.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

                // Check the response
                if (airConditionerResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }
        } catch (ParseException | ConnectorException | IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void startHumidityObservation(final String ipAddress) {
        clientHumidityObs = new CoapClient("coap://[" + ipAddress + "]/actuator/humidity");
        clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");
        clientHumidityObs.observe(
                new CoapHandler() {
                    @Override public void onLoad(CoapResponse response) {
                        handleHumidityResponse(response, ipAddress);
                    }
                    @Override public void onError() {
                        System.out.println("[-] Observing humidity failed");
                    }
                }
        );
    }

    public void handleHumidityResponse(CoapResponse response, String address) {
        System.out.println("[!] Receiving humidity notification");
        String responseString = new String(response.getPayload());
        System.out.println(" <  " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            long value = (long) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToDehumidifier(address);
            if ((value > 25 || value < 20) && !status.equals("on")){
                // send on message to dehumidifier
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the dehumidifier
                System.out.println("[!] Sending PUT request to dehumidifier");
                System.out.println(" >  " + jsonString_response);
                CoapResponse dehumidifierResponse = clientDehumidifier.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

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
                System.out.println("[!] Sending PUT request to dehumidifier");
                System.out.println(" >  " + jsonString_response);
                CoapResponse dehumidifierResponse = clientDehumidifier.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

                // Check the response
                if (dehumidifierResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }

        } catch (ParseException | ConnectorException | IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void startPresenceObservation(final String ipAddress, final int area_id) {
        clientPresenceObs = new CoapClient("coap://[" + ipAddress + "]/actuator/presence");
        clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");
        clientPresenceObs.observe(
                new CoapHandler() {
                    @Override public void onLoad(CoapResponse response) {
                        handlePresenceResponse(response, area_id, ipAddress);
                    }
                    @Override public void onError() {
                        System.out.println("[-] Observing presence failed");
                    }
                }
        );
    }

    public void handlePresenceResponse(CoapResponse response, int area_id, String address) {
        System.out.println("[!] Receiving presence notification");
        String responseString = new String(response.getPayload());
        System.out.println(" <  " + responseString);
        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            long value = (long) genreJsonObject.get("value");

            String jsonString_response;
            String status = getRequestToSemaphore(address);
            int maxPresence = FitSenseDBHandler.getMaxPresenceArea(area_id);
            if (value > maxPresence && !status.equals("on")){
                // send on message to semaphore
                jsonString_response = "{\"mode\": \"on\"}";

                // Send the PUT request to handle the semaphore
                System.out.println("[!] Sending PUT request to semaphore");
                System.out.println(" >  " + jsonString_response);
                CoapResponse semaphoreResponse = clientSemaphore.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

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
                System.out.println("[!] Sending PUT request to semaphore");
                System.out.println(" >  " + jsonString_response);
                CoapResponse semaphoreResponse = clientSemaphore.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);

                // Check the response
                if (semaphoreResponse.isSuccess()) {
                    System.out.println("[+] PUT request succeeded");
                } else {
                    System.out.println("[-] PUT request failed");
                }
            }

        } catch (ParseException | ConnectorException | IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void cancelObs(){
        clientTemperatureObs.shutdown();
        clientAirConditioner.shutdown();

        clientHumidityObs.shutdown();
        clientSemaphore.shutdown();

        clientPresenceObs.shutdown();
        clientDehumidifier.shutdown();
    }
}
