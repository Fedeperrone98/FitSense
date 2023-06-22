package it.unipi.iot.coap_client_handler;

import it.unipi.iot.db_handler.FitSenseDBHandler;

public class SemaphoreHandler implements Runnable{

    private final static MyCoapClient coapClient = MyCoapClient.getInstance();

    int detected_presence;
    String address;
    int area_id;

    public SemaphoreHandler(int detected_presence, String address, int area_id) {
        this.detected_presence = detected_presence;
        this.address = address;
        this.area_id = area_id;
    }

    @Override
    public void run() {
        String jsonString_response;
        String status = coapClient.getRequestToSemaphore(address);
        int maxPresence = FitSenseDBHandler.getMaxPresenceArea(area_id);
        if (detected_presence > maxPresence && !status.equals("on")){
            // send on message to semaphore
            jsonString_response = "{\"mode\": \"on\"}";

            // Send the PUT request to handle the semaphore
            System.out.println("[!] Sending PUT request to semaphore");
            coapClient.putCommandToSemaphore(address, jsonString_response);

        } else  if( detected_presence <= maxPresence && !status.equals("off")){
            // send off message to semaphore
            jsonString_response = "{\"mode\": \"off\"}";

            // Send the PUT request to handle the semaphore
            System.out.println("[!] Sending PUT request to semaphore");
            coapClient.putCommandToSemaphore(address, jsonString_response);
        }


    }
}
