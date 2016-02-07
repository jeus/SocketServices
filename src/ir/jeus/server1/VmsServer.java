package ir.jeus.server1;



import ir.jeus.client1.ClientWorker;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VmsServer
{

    private static final int PORT = 33000;
    public static final HashMap<String, Socket> clientsMap = new HashMap<>();
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException
    {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        //DBConnection dBConnection = new DBConnection("jdbc:mysql://46.249.121.231:3306/", "vms", "root", "browneynak");

        try
        {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT + "...");
        } catch (IOException e)
        {
            serverSocket.close();
            System.err.println("Could not listen on port:" + PORT);
            System.exit(1);
        }

        Socket clientSocket;

        try
        {

            //while (true)
            //{
            Thread readInput = new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    try
                    {
                        String input = bufferedReader.readLine();
                        while (!input.equals("\\q"))
                        {
                            input = bufferedReader.readLine();
                        }
                        serverSocket.close();
                        System.exit(1);
                    } catch (IOException ex)
                    {
                        Logger.getLogger(VmsServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            readInput.start();
            while (true)
            {
                clientSocket = serverSocket.accept();
                clientsMap.put(clientSocket.getInetAddress().getAddress() + "", clientSocket);
                System.out.println("Connection[" + clientsMap.size() + "] received from " + clientSocket.getInetAddress().getHostAddress() + ".");
                Thread thread = new Thread(new ClientWorker(clientSocket/*, dBConnection*/));
                thread.start();
            }
            //}

        } catch (IOException e)
        {
            serverSocket.close();
            System.err.println("Accept failed.");
            System.exit(1);
        }
    }
}