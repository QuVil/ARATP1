/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.DatagramPacket;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author p1507338
 */
public class RX302Server extends Com{
    
    //opens a RX302 server on port SERVER_PORT
    public RX302Server(){
        super(SERVER_PORT);
    }
    
    public void runRX302(){
        byte[] data = new byte[512];
        dp = new DatagramPacket(data, data.length);
        
        while(true){
            try{
                ds.receive(dp);
                //WAIT
                System.out.println("New message received...");
                String messageData = new String(dp.getData(), "UTF-8");
                
                if (messageData.contains("hello rx302")){
                    System.out.println("New client online : " 
                            + dp.getSocketAddress().toString());
                
                String answerString = "rx302 ready";

                send(answerString, dp.getAddress(), dp.getPort());
                }
                
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

