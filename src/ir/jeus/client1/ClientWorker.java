package ir.jeus.client1;

import ir.jeus.server1.VmsServer;
import ir.jeus.sql.DevicelogFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mojtaba
 */
public class ClientWorker implements Runnable {

    private final Socket clientSocket;
    //private DBConnection dBConnection;
    private Statement statement;
    private PrintWriter out;
    private BufferedReader in;
    private static final Logger LOGGER = Logger.getLogger(ClientWorker.class.getName());

    public ClientWorker(Socket clientSocket/*, DBConnection dBConnection*/) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine;
            String outputLine;
            out.println("Hi");
            while (clientSocket.isConnected()) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    break;
                }
                System.out.println("client:" + inputLine);
                DevicelogFacade.insertToDB(inputLine);
                outputLine = inputLine;
                out.println(outputLine);
                if (inputLine.equalsIgnoreCase("Bye.")) {
                    break;
                }
            }
            out.close();
            in.close();
            clientSocket.close();
            VmsServer.clientsMap.remove(clientSocket.getInetAddress().getAddress() + "");
            System.out.println(clientSocket.getInetAddress().getAddress() + " is closed.");

        } catch (IOException ex) {
            System.out.println(clientSocket.getInetAddress().getAddress() + " is closed. dar catche akhare akharrrrr");
        }
    }
}
