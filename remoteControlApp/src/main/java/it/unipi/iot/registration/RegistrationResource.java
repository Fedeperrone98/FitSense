package it.unipi.iot.registration;

import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class RegistrationResource extends CoapResource {
    public RegistrationResource(String name) {
        super(name);
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        String msg = exchange.getRequestText();
        String ipAddress = exchange.getSourceAddress().getHostAddress();

        System.out.println("<  " + msg);

        JSONObject genreJsonObject = null;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(msg);
            int node_id = (int) genreJsonObject.get("node_id");
            int area_id = (int) genreJsonObject.get("area_id");
            String jsonString_response;

            if (!FitSenseDBHandler.getArea(area_id)){
                jsonString_response = "{\"status\": \"error_area\"}";
            }
            else {
                if(FitSenseDBHandler.checkNodeExistance(area_id, node_id)){
                    jsonString_response = "{\"status\": \"error_id\"}";
                }
                else{
                    jsonString_response = "{\"status\": \"server_ok\"}";

                    System.out.println("[!] Insertion node in the configuration table ... ");

                    FitSenseDBHandler.insertConfiguration(area_id, node_id, ipAddress);

                    System.out.println("[!] Finish insertion node");
                }
            }

            Response response = new Response(CoAP.ResponseCode.CONTENT);
            response.setPayload(jsonString_response);
            exchange.respond(response);

            System.out.println(" > "+ jsonString_response );



        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
