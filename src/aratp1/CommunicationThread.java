/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1506450 p1507338
 */
public class CommunicationThread extends Com implements Runnable {

    private final InetAddress IP_ADDRESS;
    private final int PORT_CLIENT;
    private final int PORT;
    private String threadType;
    
    private static ArrayList<InetAddress> clientListAddr;
    private static ArrayList<Integer> clientListPort;

    public CommunicationThread (DatagramSocket ds, DatagramPacket dp, int newPort) {
		this.ds = ds;
		this.dp = dp;
		this.IP_ADDRESS = dp.getAddress();
		this.PORT_CLIENT = dp.getPort();
		this.PORT = newPort;
		this.threadType = "server";
		
		if (CommunicationThread.clientListAddr == null || CommunicationThread.clientListAddr.size() == 0){
			CommunicationThread.clientListAddr = new ArrayList<>();
			CommunicationThread.clientListPort = new ArrayList<>();
			System.out.println("Creating Client list...\n");
		}
    }
    
    public CommunicationThread (DatagramSocket ds, InetAddress adrCli, int newPort) {
		this.ds = ds;
		byte[] data = new byte[512];
		this.dp = new DatagramPacket(data, data.length);
		this.PORT_CLIENT = 0;
		this.IP_ADDRESS = adrCli;
		this.PORT = newPort;
		this.threadType = "client";
		
    }


    @Override
    public void run() {
		try {
			if (threadType == "server")
				this.runServ();
			else if (threadType == "client")
				this.runCli();
			
		} catch (IOException ex) {
		    Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

    private void runServ() throws UnsupportedEncodingException, IOException {
		while (true) {
		    try{
				String messageData = new String(dp.getData(), "UTF-8");
				
				if (messageData.contains("hello rx302")){
					System.out.println("New client online : "
							       		+ dp.getSocketAddress().toString() + "\n");
					Integer newCliPort = 0;
					
					InetAddress newCliAddress = dp.getAddress();
					
					try {
						newCliPort = Integer.parseInt(messageData.split(" ")[2].trim());
					} catch (NumberFormatException e) {
						System.out.println("Number Conversion error.");
					}
				    
				    //Adds the new client to the list
				    CommunicationThread.clientListAddr.add(newCliAddress);
				    CommunicationThread.clientListPort.add(newCliPort);
				    
				    send("rx302 ready " + this.PORT,  dp.getAddress(), dp.getPort());
				}
				else {
					System.out.println("New message received... ");
				    System.out.println(dp.getSocketAddress().toString() +
						       " says :\n" + messageData + "\n");
		
				    //Send back the message to the clients (for display AND verification sender-side)
				    broadcastMessage(messageData);
				    send(messageData, dp.getAddress(), dp.getPort());
				}
				byte[] data = new byte[512];
				dp = new DatagramPacket(data, data.length);
				ds.receive(dp);
				
		    } catch(IOException ioe){
		    	System.out.println("Server Side IOException : runtime interrupted");
		    } catch(Exception e){
		    	throw(e);
		    }
		}
    }
    
    private void broadcastMessage(String messageData){
    	for (int i=0; i<CommunicationThread.clientListAddr.size(); ++i) {
	        send(messageData, CommunicationThread.clientListAddr.get(i), 
	        	 CommunicationThread.clientListPort.get(i));
	    }
    }
    
    private void runCli() {
    	while (true) {
		    try{
		    	ds.receive(dp);
				String messageData = new String(dp.getData(), "UTF-8");
				
				System.out.println("New message received... ");
			    System.out.println(dp.getSocketAddress().toString() +
					       " says :\n" + messageData + "\n");
				
				byte[] data = new byte[512];
				dp = new DatagramPacket(data, data.length);
				
				
		    } catch(IOException ioe){
		    	System.out.println("Client side IOException : runtime interrupted");
		    } catch(Exception e){
		    	throw(e);
		    }
		}
    }
}
