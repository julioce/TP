package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import controllers.ServerController;

public class Server extends Thread {
	
	private ServerController windowController = null;
	private ServerSocket serverSocket = null;
	private String ipAddress = null;
	private Socket communicationSocket = null;
	private ArrayList<User> connectedUsers = new ArrayList<User>();
	
	public Server(ServerController paramController, String paramipAddress){
		windowController = paramController;
		ipAddress = paramipAddress;
	}
	
	@Override
	public void run() {

		recordLog(Constants.RUNNING + ipAddress);
		
		try{
			serverSocket = new ServerSocket(Constants.SERVER_PORT);  
			recordLog(Constants.SOCKET_OPEN);
			
			while(true){
				recordLog(Constants.WAITING);
				communicationSocket = serverSocket.accept();
				Connection conn = new Connection(communicationSocket, this);
				conn.start();
			}
			
		}
		catch(IOException e){
			recordLog(Constants.E_CONNECT_TO_CLIENTS);
			closeSockets();
		}
	}


	private void closeSockets(){
		try{
			communicationSocket.close();
			serverSocket.close();
		}
		catch (IOException e){
			recordLog(Constants.E_CLOSING_SOCKETS);
		}
	}
	
	public void sendMessageToClients(Message paramMessage) {
		
		recordLog(Constants.MESSAGE_FROM + paramMessage.getSender().getUsername() 
				+ " (" + paramMessage.getSender().getIpHost() 
				+ ")\n" + paramMessage.getMessageText());

		for (User user : connectedUsers) {
			sendMessage(paramMessage, user);
		}

		recordLog(Constants.TO_USERS + connectedUsers.toString());
		recordChat(paramMessage.toString());
	}

	private void sendMessage(Message message, User destination) {
		
		Socket stablishConnection = null;
		ObjectOutputStream out = null;

		try {
			stablishConnection = new Socket(destination.getIpHost(), destination.getUserPort());
			out = new ObjectOutputStream(stablishConnection.getOutputStream());

			out.writeObject(message);
			out.flush();

			out.close();
			stablishConnection.close();
		} catch (Exception e) {
			recordLog(Constants.E_SENDING_TO_CLIENTS + message);
		}
		
	}
	
	public void recordLog(String message){
		windowController.getServerFrame().getLogArea().append(message + "\n-----------------------------\n");
	}
	
	public void recordChat(String message){
		windowController.getServerFrame().getChatArea().append(message + "\n");
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
