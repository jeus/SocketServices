package ir.jeus.client1;

import java.io.*;
import java.net.*;

public class VmsClient {

    private static String HOST = "5.220.111.237";
    private static int PORT = 8079;

    public static void main(String[] args) throws IOException {
        Socket server = null;
        PrintWriter out = null;
        BufferedReader bufferedReader = null;

        try {
            server = new Socket(HOST, PORT);
            out = new PrintWriter(server.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOST + ":" + PORT);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to[" + HOST + ":" + PORT + "]");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String fromServer;
        String fromUser;

        while ((fromServer = bufferedReader.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye.")) {
                break;
            }

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser);
            }
        }
        out.close();
        bufferedReader.close();
        stdIn.close();
        server.close();
    }
}
