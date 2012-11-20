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

	public Client(ClientController controller, int port) {
		clientWindowController = controller;
		clientPort = port;
	}

	@Override
	public void run() {

		try{
			// Tries to open the port
			serverSocket = new ServerSocket(clientPort);
			
			while(true) {
				// Accepts the connection
				communicationSocket = serverSocket.accept();
				// Reads the stream and append to client
				ObjectInputStream in = new ObjectInputStream(communicationSocket.getInputStream());
				// Parses message to an Message object type
				message = (Message) in.readObject();
				// Parses the message and append to text area
				clientWindowController.getClientFrame().getChatArea().append(message.toString() + "\n");
				// Closes the socket
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
	
	public static void sendMessageToServer(String serverAddress, Message message) {
		Socket connection = null;
		ObjectOutputStream out = null;

		try {
			// Creates the output stream
			connection = new Socket(serverAddress, Constants.SERVER_PORT);
			out = new ObjectOutputStream(connection.getOutputStream());
			
			// Writes on the stream
			out.writeObject(message);
			out.flush();
			
			// Closes stream and connection
			out.close();
			connection.close();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, Constants.E_CONNECT_TO_SERVER);
			System.exit(1);
		}
	}
	
}
