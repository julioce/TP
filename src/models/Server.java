package models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import views.ServerWindow;


import controllers.ServerController;

public class Server extends Thread {
	
	private ServerController windowController = null;
	private ServerSocket serverSocket = null;
	private Socket communicationSocket = null;
	private ArrayList<User> connectedUsers = new ArrayList<User>();
	
	public Server(ServerController paramController){
		windowController = paramController;
	}
	
	public static void main(String[] args) {

		ServerWindow serverFrame = new ServerWindow();
		serverFrame.setUp();

		ServerController windowController = new ServerController(serverFrame);
		
		Server server = new Server(windowController);
		server.start();

	}
	
	@Override
	public void run() {

		recordLog(Constants.RUNNING);
		
		try{
			serverSocket = new ServerSocket(Constants.SERVER_PORT);  
			recordLog(Constants.SOCKET_OPEN);
			
			while(true){
				recordLog(Constants.WAITING);
				communicationSocket = serverSocket.accept();
				Communication conn = new Communication(communicationSocket, this);
				conn.start();
			}
			
		}
		catch(IOException e){
			recordLog(Constants.E_CONNECT_TO_CLIENTS);
			closeSockets();
			e.printStackTrace();
		}
	}


	private void closeSockets(){
		try{
			communicationSocket.close();
			serverSocket.close();
		}
		catch (IOException e){
			recordLog(Constants.E_CLOSING_SOCKETS);
			e.printStackTrace();
		}
	}
	
	public void broadcastMessage(Message paramMessage) throws IOException{
		
		recordLog(Constants.MESSAGE_FROM + paramMessage.getSender().getUsername() 
				+ " (" + paramMessage.getSender().getIpHost() 
				+ ")\n" + paramMessage.getMessageText());

		for (User user : connectedUsers) {
			sendMessage(paramMessage, user);
		}

		recordLog(Constants.TO_USERS + connectedUsers.toString());
		recordChat(paramMessage.toString());
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
