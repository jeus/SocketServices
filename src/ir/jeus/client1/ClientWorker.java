package ir.jeus.client1;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ir.jeus.server1.VmsServer;
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
public class ClientWorker implements Runnable
{

    private Socket clientSocket;
    //private DBConnection dBConnection;
    private Statement statement;
    private PrintWriter out;
    private BufferedReader in;
    private static final Logger LOGGER = Logger.getLogger(ClientWorker.class.getName());

    public ClientWorker(Socket clientSocket/*, DBConnection dBConnection*/)
    {
        this.clientSocket = clientSocket;
        /*this.dBConnection = dBConnection;

        try
        {
            statement = dBConnection.getConnection().createStatement();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
        }*/

    }

    @Override
    public void run()
    {
        try
        {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(
                    clientSocket.getInputStream()));
            String inputLine;
            String outputLine;
            out.println("silam");
            while (clientSocket.isConnected())
            {
                inputLine = in.readLine();
                if (inputLine == null)
                {
                    break;
                }
                System.out.println("client:" + inputLine);
                ///insertLocationIntoDB(inputLine);
                outputLine = inputLine;
                out.println(outputLine);
                if (inputLine.equalsIgnoreCase("Bye."))
                {
                    ///closeStatement();
                    break;
                }
            }
            out.close();
            in.close();
            clientSocket.close();
            VmsServer.clientsMap.remove(clientSocket.getInetAddress().getAddress() + "");
            System.out.println(clientSocket.getInetAddress().getAddress() + " is closed.");

        } catch (IOException ex)
        {
            System.out.println(clientSocket.getInetAddress().getAddress() + " is closed. dar catche akhare akharrrrr");
        }
    }

   /* private void insertLocationIntoDB(String message)
    {
        String[] messageParameters = message.split(",");
        if (messageParameters.length != 2)
        {
            return;
        }
        float latitude = Float.parseFloat(messageParameters[0]);
        float longitude = Float.parseFloat(messageParameters[1]);

        String query = "INSERT INTO position (latitude, longitude) VALUES(" + latitude + "," + longitude + ")";
        try
        {
            dBConnection.insert(statement, query);
        } catch (SQLException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
        }
    }
    
    private void closeStatement()
    {
        try
        {
            statement.close();
        } catch (SQLException ex)
        {
            LOGGER.log(Level.WARNING, null, ex);
        }
    }*/
}