package it.unipi.iot;

import it.unipi.iot.mqtt_client_handler.SubscriberHumidity;
import it.unipi.iot.mqtt_client_handler.SubscriberPresence;
import it.unipi.iot.mqtt_client_handler.SubscriberTemperature;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainClass {
    public static void main(String[] args) {
        try {
            SubscriberTemperature sub_temperature = new SubscriberTemperature();
            SubscriberHumidity sub_humidity = new SubscriberHumidity();
            SubscriberPresence sub_presence = new SubscriberPresence();
        } catch(MqttException me) {
            me.printStackTrace();
        }

    }
}
