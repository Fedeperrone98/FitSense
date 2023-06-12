package it.unipi.iot;

import it.unipi.iot.control_logic.DataLogicController;
import it.unipi.iot.registration.RegistrationServer;
import it.unipi.iot.userControl.UserInputController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainClass {
    public static void main(String[] args) {
        // start thread for registration server execution
        RegistrationServer registrationServer = new RegistrationServer();
        Thread registrationServerThread = new Thread(registrationServer);
        registrationServerThread.start();

        // start thread to periodically check measurement stored on db, and implement control logic
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        DataLogicController dataLogicController = new DataLogicController();
        executor.scheduleAtFixedRate(dataLogicController, 0, 10, TimeUnit.MINUTES);

        // start thread for user command listener
        UserInputController userInputController = new UserInputController();
        Thread userInputControllerThread = new Thread(userInputController);
        userInputControllerThread.start();
    }
}
