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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author p1506450
 */
public class CommunicationThread extends Com implements Runnable {

    private InetAddress IP_ADDRESS;
    private int PORT_CLIENT;
    private int PORT;

    public CommunicationThread (DatagramSocket ds, DatagramPacket dp, int newPort) {
	this.ds = ds;
	this.dp = dp;
	this.IP_ADDRESS = dp.getAddress();
	this.PORT_CLIENT = dp.getPort();
	this.PORT = newPort;
    }


    @Override
    public void run() {
	try {
	    this.runServ();
	} catch (IOException ex) {
	    Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private void runServ() throws UnsupportedEncodingException, IOException {
	while (true) {
	    try{
		System.out.println("New message received... " + this.IP_ADDRESS.getHostName());
		String messageData = new String(dp.getData(), "UTF-8");
		if (messageData.contains("hello rx302")){
		    System.out.println("New client online : "
				       + dp.getSocketAddress().toString());
		    send("rx302 ready " + this.PORT,  dp.getAddress(), dp.getPort());
		}
		else {
		    System.out.println(dp.getSocketAddress().toString() +
				       " says :\n" + messageData + "\n");

		    //Send back the message to the client for confirmation
		    send(messageData + "lol", dp.getAddress(), dp.getPort());
		}
		byte[] data = new byte[512];
		dp = new DatagramPacket(data, data.length);
		ds.receive(dp);
	    } catch(IOException ioe){
		System.out.println("IFException : runtime interrupted");
	    } catch(Exception e){
		throw(e);
	    }
	}
    }
}
