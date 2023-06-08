package it.unipi.iot.registration;

import org.eclipse.californium.core.CoapServer;

import java.net.SocketException;

public class RegistrationServer extends CoapServer {
    public RegistrationServer() throws SocketException {
        this.add(new RegistrationResource("registration"));
    }
}
