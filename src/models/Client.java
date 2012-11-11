package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import views.ClientNicknamePopup;
import views.ClientWindow;

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
	
	public static void main(String[] args) {

		ClientNicknamePopup clientNickPrompter = new ClientNicknamePopup();
		String ipAddress = "127.0.0.1";
		int port = 1234;
		String username = clientNickPrompter.askNickname();
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, Constants.E_CONNECT_TO_SERVER);
			System.exit(1);
		}
		
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.setUp(username);

		ClientController windowController = new ClientController(clientFrame, ipAddress, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		User user = new User(ipAddress, username, port);
		Client client = new Client(windowController, username, port);
		client.start();
		
		Message message = new Message(user, Constants.CLIENT_LOGIN);
		Client.sendMessageToServer(message);
	}

	@Override
	public void run() {

		try{
			serverSocket = new ServerSocket(clientPort);
			
			while(true) {
				communicationSocket = serverSocket.accept();
				ObjectInputStream in;
				in = new ObjectInputStream(communicationSocket.getInputStream());
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
			try{
				communicationSocket.close();
				serverSocket.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null, Constants.E_CLOSING_SOCKETS);
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
			JOptionPane.showMessageDialog(null, Constants.E_CONNECT_TO_SERVER);
			System.exit(1);
		}
	}
	
}
