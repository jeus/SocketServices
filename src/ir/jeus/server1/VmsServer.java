package ir.jeus.server1;

import ir.jeus.client1.ClientWorker;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VmsServer {

    private static String hostPort;
    private static String dbName;
    private static String user;
    private static String pass;
    private static int ServerPort;

    public static final HashMap<String, Socket> clientsMap = new HashMap<>();
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException {
        hostPort = args[0];
        dbName = args[1];
        user = args[2];
        pass = args[3];
        ServerPort = Integer.parseInt(args[4]);

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            serverSocket = new ServerSocket(ServerPort);
            System.out.println("Server is listening on port " + ServerPort + "...");
        } catch (IOException e) {
            serverSocket.close();
            System.err.println("Could not listen on port:" + ServerPort);
            System.exit(1);
        }

        Socket clientSocket;

        try {
            Thread readInput = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String input = bufferedReader.readLine();
                        while (!input.equals("\\q")) {
                            input = bufferedReader.readLine();
                        }
                        serverSocket.close();
                        System.exit(1);
                    } catch (IOException ex) {
                        Logger.getLogger(VmsServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            readInput.start();
            while (true) {
                clientSocket = serverSocket.accept();
                clientsMap.put(clientSocket.getInetAddress().getAddress() + "", clientSocket);
                System.out.println("Connection[" + clientsMap.size() + "] received from " + clientSocket.getInetAddress().getHostAddress() + ".");
                Thread thread = new Thread(new ClientWorker(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            serverSocket.close();
            System.err.println("Accept failed.");
            System.exit(1);
        }
 
    
    }

    public static String getHostPort() {
        return hostPort;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}
