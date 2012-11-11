package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import views.ClientNicknamePopup;
import views.ClientWindow;


import controllers.ClientController;

public class Client extends Thread {

	private static ClientController clientWindowController = null;
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
		String username = clientNickPrompter.askNickname();
		
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.setUp(username);

		ClientController windowController = new ClientController(clientFrame, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		int port = 1234;
		
		User user = new User("127.0.0.1", username, port);
		Message message = new Message(user, Constants.CLIENT_LOGIN);
		
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
			JOptionPane.showMessageDialog(null, Constants.CONNECT_TO_CLIENT);
			System.exit(0);
		}
		finally{
			try{
				communicationSocket.close();
				serverSocket.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, Constants.CLOSING_SOCKETS);
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
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, Constants.CONNECT_TO_CLIENT);
			System.exit(0);
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
