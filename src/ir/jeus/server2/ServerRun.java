/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.jeus.server2;

/**
 *
 * @author jeus
 */
public class ServerRun {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MultiThreadedServer server = new MultiThreadedServer(8079);
        new Thread(server).start();
        try {
            Thread.sleep(365000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
    }

}
