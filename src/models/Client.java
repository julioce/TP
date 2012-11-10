package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import main.Constants;

import controllers.ClientController;


public class Client extends Thread {

	private ClientController clientWindowController = null;
	private String username = null;
	private int clientPort;
	
	private ServerSocket serverSocket = null;
	private Socket communicationSocket = null;

	public Client(ClientController paramController, String paramUsername, int paramPort) {
		username = paramUsername;
		clientWindowController = paramController;
		clientPort = paramPort;
	}

	@Override
	public void run() {

		System.out.println("Client running!");
		
		try{
			serverSocket = new ServerSocket(clientPort);  
			System.out.println("Server socket started");
			
			while(true) {
				System.out.println("Waiting for accept");
				communicationSocket = serverSocket.accept();
				System.out.println("Accepted.");
				ClientMessageReceiver messageReceiver = new ClientMessageReceiver(communicationSocket, this);
				messageReceiver.start();
			}
			
		}
		catch(IOException e) {  
			System.err.println("Exception while accepting the connection to a client");
			e.printStackTrace();
		}
		finally{
			try{
				communicationSocket.close();
				serverSocket.close();
				
				System.out.println("Client's sockets closed correctly.");
			}
			catch (IOException e){
				System.err.println("Exception trying to close the sockets");
				e.printStackTrace();
			}
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
		catch (ConnectException e) {
			e.printStackTrace();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e)  {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void sendMessage(Message message, User destination) {
		Socket stablishConnection = null;
		ObjectOutputStream out = null;

		try {
			stablishConnection = new Socket(destination.getIpHost(), Constants.SERVER_PORT);
			out = new ObjectOutputStream(stablishConnection.getOutputStream());

			out.writeObject(message);
			out.flush();

			out.close();
			stablishConnection.close();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public ClientController getWindowController() {
		return clientWindowController;
	}

	public void setClientWindowController(ClientController paramWindowController) {
		clientWindowController = paramWindowController;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String paramUsername) {
		username = paramUsername;
	}
	
}
