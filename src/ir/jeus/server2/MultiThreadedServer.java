package ir.jeus.server2;

/**
 *
 * @author jeus
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import sql.JDBCExample;

public class MultiThreadedServer implements Runnable {

    protected int serverPort = 8079;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public MultiThreadedServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                System.out.println("*****Multithread******----------------------------------");
                String str  = in.readUTF();
                System.out.println("*****Multithread******"+str);
                JDBCExample.insertToDB(str);
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                out.writeUTF("Thank you for connecting to "+ clientSocket.getLocalSocketAddress() + "\nGoodbye!");
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server")).start();
        }
        System.out.println("Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port"+serverPort, e);
        }
    }

}
