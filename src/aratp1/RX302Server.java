/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.DatagramPacket;
import java.io.IOException;
import java.util.ArrayList;

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
                System.out.println("New message received...");
                String messageData = new String(dp.getData(), "UTF-8");
                
                boolean escapeConnectionMessage = false;
                
                //Case : The message is an initial connection message
                if (messageData.contains("hello rx302")){
                    System.out.println("New client online : " 
                            + dp.getSocketAddress().toString() + "\n");
                    
                    //Escape of case where an already connected client sends
                    //the initial connection message
                    if (connectedClients.contains(
                            dp.getSocketAddress().toString())){
                        escapeConnectionMessage = true;
                    }else{
                        connectedClients.add(dp.getSocketAddress().toString());
                        
                        String answerString = "rx302 ready";
                        //answerString triggers the client to know that 
                        //connection was successful
                        send(answerString, dp.getAddress(), dp.getPort());
                    }
                    
                } //Case : The message is from a connected client - displays it
                else if(connectedClients.contains(
                        dp.getSocketAddress().toString()) 
                        || escapeConnectionMessage){
                    System.out.println(dp.getSocketAddress().toString() +
                            " says :\n" + messageData + "\n");
                    
                    //Send back the message to the client for confirmation
                    send(messageData, dp.getAddress(), dp.getPort());
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

