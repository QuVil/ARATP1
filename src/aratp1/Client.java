/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author p1507338
 */
public class Client extends Com{
    
    public Client(){
        super();
    }
    
    //connects to default server (localhost, port 6969)
    public void connectToServer(){
        String connectMessage = "hello rx302";
        
        try{
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            send(connectMessage, serverAddress, SERVER_PORT);
            
            byte[] data = new byte[512];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try{
                ds.receive(dp);
                //WAIT
                System.out.println("New message received...");
                String messageData = new String(dp.getData(), "UTF-8");
                
                if (messageData.contains("rx302 ready")){
                    System.out.println("Connection successful @ " 
                            + dp.getSocketAddress().toString());
                }
            }catch(IOException e){
                System.out.println("IOException : runtime interrupted");
            }
            
        }catch(UnknownHostException e){
            System.out.println("Unkown Host Exception : runtime interrupted");
        }
    }
    
    public static void main(String[] args) {
        Client cli = new Client();
        cli.connectToServer();
    }
}
