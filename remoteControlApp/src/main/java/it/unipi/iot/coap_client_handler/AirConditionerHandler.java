package it.unipi.iot.coap_client_handler;

public class AirConditionerHandler implements Runnable {

    private final static MyCoapClient coapClient = MyCoapClient.getInstance();

    int detected_temperature;
    String address;

    public AirConditionerHandler(int temperature, String add) {
        detected_temperature = temperature;
        address = add;
    }

    @Override
    public void run() {
        String jsonString_response;
        String status = coapClient.getRequestToAirConditioner(address);
        if ((detected_temperature > 24 || detected_temperature < 19) && !status.equals("on")) {
            // send on message to air conditioner
            jsonString_response = "{\"mode\": \"on\"}";

            // Send the PUT request to handle the air conditioner
            System.out.println("[!] Sending PUT request to air conditioner");
            coapClient.putCommandToAirConditioner(address, jsonString_response);

        } else if ((detected_temperature <= 24 || detected_temperature >= 19) && !status.equals("off")) {
            // send off message to air conditioner
            jsonString_response = "{\"mode\": \"off\"}";

            // Send the PUT request to handle the air conditioner
            System.out.println("[!] Sending PUT request to air conditioner");
            coapClient.putCommandToAirConditioner(address, jsonString_response);
        }
    }
}
