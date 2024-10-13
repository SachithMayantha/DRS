package com.example.drsystemserver;

import com.example.drsystemserver.util.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author sachi
 * multithreaded main class for application server
 */
public class DRSystemServer {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private volatile boolean running = true; 

    // Constructor that initializes the server on a given port
    public DRSystemServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        threadPool = Executors.newFixedThreadPool(20);
        System.out.println("Server running on port - " + port);
    }

    // Method to start the server and listen for client connections
    public void start() {
        try {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    // Submit each client request to the thread pool
                    threadPool.submit(new ClientHandler(clientSocket));
                } catch (SocketException e) {
                    if (!running) {
                        System.out.println("Server is shutting down...");
                        break; // Exit the loop if server is stopped
                    }
                    System.err.println("Socket error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    // Method to stop the server
    public void stop() {
        running = false;
        try {
            serverSocket.close();  // This will interrupt the accept() call
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }

    // Method to gracefully shut down the server and release resources
    private void shutdown() {
        try {
            System.out.println("Shutting down thread pool...");
            threadPool.shutdown(); // Disable new tasks from being submitted
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) { // Wait for existing tasks to finish
                System.err.println("Thread pool did not terminate in the allotted time.");
                threadPool.shutdownNow(); // Cancel currently executing tasks
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Thread pool did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            threadPool.shutdownNow(); // Re-cancel if the current thread is interrupted
            Thread.currentThread().interrupt(); // Preserve interrupt status
        } finally {
            try {
                serverSocket.close(); // Close server socket when shutdown is complete
                System.out.println("Server shut down.");
            } catch (IOException e) {
                System.err.println("Error during server shutdown: " + e.getMessage());
            }
        }
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            DRSystemServer server = new DRSystemServer(9200);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown hook triggered.");
                server.stop();
            }));
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }
}
