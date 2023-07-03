package it.unipi.iot.mqtt_client_handler;

import it.unipi.iot.db_handler.FitSenseDBHandler;
import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class SubscriberTemperature implements MqttCallback {

    String topic = "temperature";
    String broker = "tcp://127.0.0.1:1883";
    String clientId = "TemperatureSubscriber";

    public SubscriberTemperature() throws MqttException {
        MqttClient mqttClient = new MqttClient(broker,clientId);
        mqttClient.setCallback( this );
        mqttClient.connect();
        System.out.println("[!] Subscribing "+ topic + " topic");
        mqttClient.subscribe(topic);
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage){

        System.out.println("[!] Receiving temperature message");
        String msg = new String(mqttMessage.getPayload());
        System.out.println(" <  " + msg);

        JSONObject genreJsonObject;
        try {
            genreJsonObject = (JSONObject) JSONValue.parseWithException(msg);
            long value = (long) genreJsonObject.get("value");
            long area_id = (long) genreJsonObject.get("area_id");
            long node_id = (long) genreJsonObject.get("node_id");

            System.out.println("[!] Insert temperature measurement into database");
            FitSenseDBHandler.insertTemperature((int)area_id,(int)node_id,(int)value);
        }catch (ParseException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
