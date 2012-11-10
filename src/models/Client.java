package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import views.ClientNicknamePopup;
import views.ClientWindow;


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
	
	public static void main(String[] args) {

		ClientNicknamePopup clientNickPrompter = new ClientNicknamePopup();
		String username = clientNickPrompter.promptForNick();
		
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.setUp(username);

		ClientController windowController = new ClientController(clientFrame, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		int port = 1234;
		
		User user = new User("127.0.0.1", username, port);
		Message message = new Message(user, "In the house!");
		
		Client client = new Client(windowController, username, port);
		client.start();
		
		Client.sendMessageToServer(message);
	}

	@Override
	public void run() {

		try{
			serverSocket = new ServerSocket(clientPort);
			
			while(true) {
				communicationSocket = serverSocket.accept();
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
