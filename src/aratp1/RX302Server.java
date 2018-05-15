/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.DatagramPacket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 *
 * @author p1507338
 */
public class RX302Server extends Com{
    
    
    //opens a RX302 server on port DEFAULT_SERVER_PORT
    public RX302Server(){
        super(DEFAULT_SERVER_PORT);
    }
    
    public void runRX302(){
        while(true){
            byte[] data = new byte[512];
            dp = new DatagramPacket(data, data.length);
            
            try{
                ds.receive(dp);
                //WAIT

                ArrayList<Integer> listPort = this.scan(1, 65000);
                Integer newPort = listPort.get(listPort.size() -1);
                DatagramSocket newSock = new DatagramSocket(newPort);
                CommunicationThread CT = new CommunicationThread(newSock, dp);
                System.out.println(dp.getData());
                CT.run();
            }catch(IOException ioe){
                System.out.println("IOException : runtime interrupted");
            }
        }
    }
    
    public static void main(String[] args) {
        RX302Server serv = new RX302Server();
        serv.runRX302();
    }
}

