package it.unipi.iot.registration;

import it.unipi.iot.coap_client_handler.MyCoapClient;
import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

// Define server resource
public class RegistrationResource extends CoapResource {

    private final static MyCoapClient coapClient = MyCoapClient.getInstance();

    public RegistrationResource(String name) {
        super(name);
    }

    @Override
    /*
     * Function to handle the registration requests from coap-nodes
     * CoapExchange is used to retrieve information on the request
     */
    public void handlePOST(CoapExchange exchange) {
        System.out.println("[!] Receiving POST request");

        String msg = exchange.getRequestText();
        String ipAddress = exchange.getSourceAddress().getHostAddress();

        System.out.println("<  " + msg);

        try {
            String jsonString_response;

            // Obtain information from json message
            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(msg);
            long node_id = (long) genreJsonObject.get("node_id");
            long area_id = (long) genreJsonObject.get("area_id");

            // Check the existence of the area_id
            if (!FitSenseDBHandler.checkAreaExistence((int)area_id)){
                jsonString_response = "{\"status\": \"error_area\"}";
            }
            else {
                // Check the presence of a node characterized with the same Ids
                if(FitSenseDBHandler.checkNodeExistence((int)area_id, (int) node_id)){
                    jsonString_response = "{\"status\": \"error_id\"}";
                }
                else{
                    jsonString_response = "{\"status\": \"server_ok\"}";

                    System.out.println("[!] Insertion node in the configuration table ... ");

                    FitSenseDBHandler.insertConfiguration((int)area_id, (int)node_id, ipAddress);

                    System.out.println("[!] Finish insertion node");

                    coapClient.startTemperatureObservation(ipAddress);
                    coapClient.startHumidityObservation(ipAddress);
                    coapClient.startPresenceObservation(ipAddress, (int)area_id);
                }
            }

            Response response = new Response(CoAP.ResponseCode.CONTENT);
            response.setPayload(jsonString_response);
            exchange.respond(response);

            System.out.println(" > "+ jsonString_response );

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("[-] error during message parsing");
        }

    }
}
