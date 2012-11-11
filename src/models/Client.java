package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import controllers.ClientController;

public class Client extends Thread {

	private static ClientController clientWindowController = null;
	private Message message = null;
	private int clientPort;
	private ServerSocket serverSocket = null;
	private Socket communicationSocket = null;

	public Client(ClientController paramController, String paramUsername, int paramPort) {
		clientWindowController = paramController;
		clientPort = paramPort;
	}

	@Override
	public void run() {

		try{
			serverSocket = new ServerSocket(clientPort);
			
			while(true) {
				communicationSocket = serverSocket.accept();
				ObjectInputStream in = new ObjectInputStream(communicationSocket.getInputStream());
				message = (Message) in.readObject();
				clientWindowController.getClientFrame().getChatArea().append(message.toString() + "\n");
				communicationSocket.close();
			}
			
		}
		catch(Exception e) { 
			JOptionPane.showMessageDialog(null, Constants.E_CONNECT_TO_SERVER);
			clientWindowController.windowClosing(null);
			System.exit(1);
		}
		finally{
			closeSockets();
		}
	}
	
	private void closeSockets(){
		try{
			communicationSocket.close();
			serverSocket.close();
		}
		catch (IOException e){
			JOptionPane.showMessageDialog(null, Constants.E_CLOSING_SOCKETS);
			System.exit(1);
		}
	}
	
	public static void sendMessageToServer(Message message) {
		Socket stablishConnection = null;
		ObjectOutputStream out = null;

		try {
			stablishConnection = new Socket(Constants.SERVER_IP, Constants.SERVER_PORT);
			out = new ObjectOutputStream(stablishConnection.getOutputStream());

			out.writeObject(message);
			out.flush();
			
			out.close();
			stablishConnection.close();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, Constants.E_CONNECT_TO_SERVER);
			System.exit(1);
		}
	}
	
}
