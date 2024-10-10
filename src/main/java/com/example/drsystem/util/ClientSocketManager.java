package com.example.drsystem.util;

import java.io.*;
import java.net.Socket;

public class ClientSocketManager implements AutoCloseable {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9200;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientSocketManager() throws IOException {
        connectToServer();
    }

    // Establish connection to the server
    private void connectToServer() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    // Send a request to the server and receive the response
    public Object sendRequest(String requestType, Object... params) throws IOException, ClassNotFoundException {
        output.writeObject(requestType); // Send the type of request 

        // Send parameters 
        for (Object param : params) {
            output.writeObject(param);
        }

        // Receive the server's response
        return input.readObject();
    }

    // Close the socket when done
    @Override
    public void close() throws Exception {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }
}