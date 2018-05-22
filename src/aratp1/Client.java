/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aratp1;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author p1507338 p1506450
 */
public class Client extends Com{

	public Client(){
		super();    //Opens the highest available socket found
	}
        
    /* Standard connection procedure. Sends the default connection message
    /  to the default Server IP and Port.
    /  If the connection is successful, the server port is returned.
    */
	public int connectToServer(){
		DatagramSocket newSock = this.scan(1, 65000);
		Integer newPort = newSock.getLocalPort();
		
		String connectMessage = "hello rx302 " + newPort;
		int currentServerport = 0;
		
		try{
			InetAddress serverAddress = InetAddress.getByName(DEFAULT_SERVER_IP);
			send(connectMessage, serverAddress, DEFAULT_SERVER_PORT);

			byte[] data = new byte[512];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			ds.receive(dp);
			
			//WAIT
			System.out.println("New message received...");
			String messageData = new String(dp.getData(), "UTF-8");

			if (messageData.contains("rx302 ready")){
				System.out.println("Connection successful @ "
						+ dp.getSocketAddress().toString());
				
				CommunicationThread CT = new CommunicationThread(
						newSock, ds.getInetAddress(), newPort);
				//Note : ds.getInetAddress() returns null. Fortunately, it is a useless parameter.
				new Thread(CT).start();
				
				try {
					currentServerport = Integer.parseInt(messageData.split(" ")[2].trim());
				} catch (NumberFormatException e) {
					System.out.println("Number Conversion error.");
				}
			}
		} catch(IOException e){
			System.out.println("IWException : runtime interrupted");
		}
		return currentServerport;
	}

	public void runClient(){
		int port = connectToServer();

		while(true){
			Scanner s = new Scanner(System.in);
			String messageText = s.nextLine();

			if (!messageText.isEmpty()){
				try{
					InetAddress serverAddress =
						InetAddress.getByName(DEFAULT_SERVER_IP);
					send(messageText, serverAddress, port);

					//wait for confirmation from the server 
                    //that the message has been received
					byte[] data = new byte[512];
					dp = new DatagramPacket(data, data.length);
					ds.receive(dp);

					String messageData = new String(dp.getData(), "UTF-8")
						.trim();

					if (messageData.equals(messageText)){
						System.out.println("Message sent successfully.\n");
					}

				}catch(UnknownHostException uhe){
					System.out.println("UnknownHost : runtime interrupted");
				}catch(IOException ioe){
					System.out.println("IOException : runtime interrupted");
				}
			}
		}
	}

	public static void main(String[] args) {
		Client cli = new Client();

		cli.runClient();
	}
}
