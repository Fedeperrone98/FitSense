package it.unipi.iot.registration;

import org.eclipse.californium.core.CoapServer;

import java.net.SocketException;

public class RegistrationServer extends CoapServer {
    public RegistrationServer() throws SocketException {
        System.out.println("[!] Start registration server ...");

        RegistrationServer server = new RegistrationServer();
        server.add(new RegistrationResource("registration"));
        server.start();
    }
}
