/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.DatagramPacket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author p1507338
 */
public class RX302Server extends Com{
    
    private ArrayList<String> connectedClients;
    
    //opens a RX302 server on port DEFAULT_SERVER_PORT
    public RX302Server(){
        super(DEFAULT_SERVER_PORT);
        
        connectedClients = new ArrayList<>();
    }
    
    public void runRX302(){
        while(true){
            byte[] data = new byte[512];
            dp = new DatagramPacket(data, data.length);
            
            try{
                ds.receive(dp);
                //WAIT
                ArrayList<Integer> listPort = Com.scan(1, 65000);
                Integer newPort = listPort.get(listPort.size() -1);
		System.out.println(newPort);
                DatagramSocket newSock = new DatagramSocket(newPort);
                CommunicationThread CT = new CommunicationThread(newSock, dp, newPort);
                CT.run();
            }catch(IOException ioe){
		    ioe.printStackTrace();
                System.out.println("IOException : runtime interrupted");
            }
        }
    }
    
    public static void main(String[] args) {
        RX302Server serv = new RX302Server();
        serv.runRX302();
    }
}

