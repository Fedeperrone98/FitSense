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
    private CoapClient clientTemperatureObs = null;
    private CoapClient clientAirConditioner = null;
    private CoapClient clientHumidityObs = null;
    private CoapClient clientDehumidifier = null;
    private CoapClient clientPresenceObs = null;
    private CoapClient clientSemaphore = null;

    public static MyCoapClient getInstance() {
        if (instance == null)
            instance = new MyCoapClient();
        return instance;
    }

    public void startTemperatureObservation(String ipAddress) {
        clientTemperatureObs = new CoapClient("coap://[" + ipAddress + "]/actuator/temperature");
        clientAirConditioner = new CoapClient("coap://[" + ipAddress + "]/actuator/air_conditioner");
        clientTemperatureObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handleTemperatureResponse(response);
                    }

                    public void onError() {
                        System.out.println("[-] Observing temperature failed");
                    }
                }
        );
    }

    public void handleTemperatureResponse(CoapResponse response){
        System.out.println("[!] Receiving temperature notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject = null;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            if (value > 24 || value < 19){
                // send on message to air conditioner
                jsonString_response = "{\"mode\": \"on\"}";
            } else {
                // send off message to air conditioner
                jsonString_response = "{\"mode\": \"off\"}";
            }

            // Send the PUT request
            CoapResponse airConditionerResponse = clientAirConditioner.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" > " + jsonString_response);

            // Check the response
            if (airConditionerResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }

        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startHumidityObservation(String ipAddress) {
        clientHumidityObs = new CoapClient("coap://[" + ipAddress + "]/actuator/humidity");
        clientDehumidifier = new CoapClient("coap://[" + ipAddress + "]/actuator/dehumidifier");
        clientHumidityObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handleHumidityResponse(response);
                    }

                    public void onError() {
                        System.out.println("[-] Observing humidity failed");
                    }
                }
        );
    }

    public void handleHumidityResponse(CoapResponse response) {
        System.out.println("[!] Receiving humidity notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject = null;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            if (value > 25 || value < 20){
                // send on message to dehumidifier
                jsonString_response = "{\"mode\": \"on\"}";
            } else {
                // send off message to dehumidifier
                jsonString_response = "{\"mode\": \"off\"}";
            }

            // Send the PUT request
            CoapResponse dehumidifierResponse = clientDehumidifier.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" > " + jsonString_response);

            // Check the response
            if (dehumidifierResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }

        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startPresenceObservation(String ipAddress, int area_id) {
        clientPresenceObs = new CoapClient("coap://[" + ipAddress + "]/actuator/presence");
        clientSemaphore = new CoapClient("coap://[" + ipAddress + "]/actuator/semaphore");
        clientPresenceObs.observe(
                new CoapHandler() {
                    public void onLoad(CoapResponse response) {
                        handlePresenceResponse(response, area_id);
                    }

                    public void onError() {
                        System.out.println("[-] Observing presence failed");
                    }
                }
        );
    }

    public void handlePresenceResponse(CoapResponse response, int area_id) {
        System.out.println("[!] Receiving presence notification");
        String responseString = new String(response.getPayload());
        System.out.println(" < " + responseString);
        JSONObject genreJsonObject = null;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(responseString);
            int value = (int) genreJsonObject.get("value");

            String jsonString_response;
            int maxPresence = FitSenseDBHandler.getMaxPresenceArea(area_id);
            if (value > maxPresence){
                // send on message to semaphore
                jsonString_response = "{\"mode\": \"on\"}";
            } else {
                // send off message to semaphore
                jsonString_response = "{\"mode\": \"off\"}";
            }

            // Send the PUT request
            CoapResponse semaphoreResponse = clientSemaphore.put(jsonString_response, MediaTypeRegistry.TEXT_PLAIN);
            System.out.println(" > " + jsonString_response);

            // Check the response
            if (semaphoreResponse.isSuccess()) {
                System.out.println("[+] PUT request succeeded");
            } else {
                System.out.println("[-] PUT request failed");
            }

        } catch (ParseException | ConnectorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
