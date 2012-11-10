package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import main.Constants;

import controllers.ServerController;

public class Server extends Thread {
	
	private ServerController windowController = null;
	private ServerSocket serverSocket = null;   
	private Socket communicationSocket = null;  
	
	private ArrayList<User> connectedUsers = new ArrayList<User>();
	
	public Server(ServerController paramController){
		windowController = paramController;
	}
	
	@Override
	public void run() {

		System.out.println("Server running!");
		
		try{
			serverSocket = new ServerSocket(Constants.SERVER_PORT);  
			System.out.println("Server socket opened!");
			
			while(true){
				System.out.println("Waiting for client to connect...");
				communicationSocket = serverSocket.accept();
				Communication conn = new Communication(communicationSocket, this);
				conn.start();
				
			}
			
		}
		catch(BindException e){
			closeSockets();
			System.exit(1);
		}
		catch(IOException e){
			System.out.println("Exception while accepting the connection to a client");
			e.printStackTrace();
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
			System.out.println("Exception trying to close the sockets");
			e.printStackTrace();
		}
	}
	
	public void broadcastMessage(Message paramMessage) throws IOException{
		System.out.println("Message to broadcast: " + paramMessage.getMessageText());
		System.out.println("From IP: " + paramMessage.getSender().getIpHost());

		System.out.println("Retrieving connected users...");
		System.out.println(connectedUsers);

		for (User user : connectedUsers) {
			System.out.println("For user: " + user.getUsername());
			sendMessage(paramMessage, user);
		}
		
		windowController.getServerFrame().getChatArea().append(paramMessage.toString() + "\n");
	}

	private static void sendMessage(Message message, User destination) throws IOException {
		
		Socket stablishConnection = null;
		ObjectOutputStream out = null;

		stablishConnection = new Socket(destination.getIpHost(), destination.getUserPort());
		out = new ObjectOutputStream(stablishConnection.getOutputStream());

		out.writeObject(message);
		out.flush();

		out.close();
		stablishConnection.close();
	}
	
	public ArrayList<User> getConnectedUsers() {
		return connectedUsers;
	}

	public void setConnectedUsers(ArrayList<User> paramConnectedUsers) {
		connectedUsers = paramConnectedUsers;
	}

	public ServerController getWindowController() {
		return windowController;
	}

	public void setWindowController(ServerController paramWindowController) {
		windowController = paramWindowController;
	}

}
