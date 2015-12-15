package com.platform.iot.api.console;

import com.platform.iot.api.Config;
import com.platform.iot.api.TopicDistributionApplication;
import com.platform.iot.api.bussiness.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
  */
public class Console implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Console.class);

    public class ConsoleProtocol {

        public String processInput(String theInput) {
            String theOutput = null;

            if (theInput == null) {
                theOutput = "Welcome!";
            } else if (theInput.equalsIgnoreCase("Shutdown")) {
                try {
                    TopicDistributionApplication.getRunningInstance().shutdown();
                    theOutput = "Shutdown";
                } catch (Exception e) {
                    logger.error("Exception occurred while shutdown", e);
                    theOutput = "Exception occurred: " + e.getMessage();
                }
            } else if (theInput.equalsIgnoreCase("Bye")) {
                theOutput = "Bye";
            } else if (theInput.equalsIgnoreCase("Migrate")) {
                try {
                    List<User> notNotifiedUsers = TopicDistributionApplication.getRunningInstance().migrate();
                    if (!notNotifiedUsers.isEmpty()) {
                        StringBuilder sb = new StringBuilder("Migrate status:");
                        for (User user : notNotifiedUsers) {
                            sb.append(" " + user);
                        }
                        theOutput = sb.toString();
                    } else {
                        theOutput = "Migrated";
                    }
                } catch (Exception e) {
                    logger.error("Exception occurred while migrating", e);
                    theOutput = "Exception occurred: " + e.getMessage();
                }
            } else if (theInput.equalsIgnoreCase("freeze")) {
                //stop server from accepting new connections
                TopicDistributionApplication.getRunningInstance().freeze();
                theOutput = "freezed";
            } else if (theInput.equalsIgnoreCase("unfreeze")) {
                //allow server to accept new connections
                TopicDistributionApplication.getRunningInstance().unfreeze();
                theOutput = "unfreezed";
            }

            return theOutput;
        }
    }

    @Override
    public void run() {
        int portNumber = Config.INSTANCE.getConsolePort();

        boolean shutdown = false;
        while (!shutdown) {
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String inputLine, outputLine;

                ConsoleProtocol consoleProtocol = new ConsoleProtocol();
                outputLine = consoleProtocol.processInput(null);
                out.println(outputLine);
                while ((inputLine = in.readLine()) != null) {
                    outputLine = consoleProtocol.processInput(inputLine);
                    out.println(outputLine);
                    if (outputLine.equals("Shutdown")) {
                        shutdown = true;
                        break;
                    } else if (outputLine.equals("Bye")) {
                        break;
                    }
                }
            } catch (IOException e) {
                logger.error("io exception", e);
            }
        }
    }

}
