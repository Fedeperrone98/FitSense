package it.unipi.iot.coap_client_handler;

public class DehumidifierHandler implements Runnable{

    private final static MyCoapClient coapClient = MyCoapClient.getInstance();

    int detected_humidity;
    String address;

    public DehumidifierHandler(int detected_humidity, String address) {
        this.detected_humidity = detected_humidity;
        this.address = address;
    }

    @Override
    public void run() {

        String jsonString_response;
        String status = coapClient.getRequestToDehumidifier(address);
        if ((detected_humidity > 25 || detected_humidity < 20) && !status.equals("on")){
            // send on message to dehumidifier
            jsonString_response = "{\"mode\": \"on\"}";

            // Send the PUT request to handle the dehumidifier
            System.out.println("[!] Sending PUT request to dehumidifier");
            coapClient.putCommandToDehumidifier(address, jsonString_response);

        } else if( (detected_humidity <= 25 || detected_humidity >= 20) && !status.equals("off")){
            // send off message to dehumidifier
            jsonString_response = "{\"mode\": \"off\"}";

            // Send the PUT request to handle the dehumidifier
            System.out.println("[!] Sending PUT request to dehumidifier");
            coapClient.putCommandToDehumidifier(address, jsonString_response);
        }


    }
}
