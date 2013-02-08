package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import controllers.ServerController;

public class Server extends Thread {
	
	private ServerController windowController = null;
	private ServerSocket serverSocket = null;
	private Socket communicationSocket = null;
	private ArrayList<User> connectedUsers = new ArrayList<User>();
	
	public Server(ServerController paramController, String paramipAddress){
		windowController = paramController;
	}
	
	@Override
	public void run() {

		recordLog(Constants.RUNNING);
		
		try{
			// Tries to open the port
			serverSocket = new ServerSocket(Constants.SERVER_PORT);  
			recordLog(Constants.SOCKET_OPEN);
			Message message;
			
			while(true){
				// Accepts the connection
				communicationSocket = serverSocket.accept();
				recordLog(Constants.WAITING);
				// Reads the stream and append to client
				ObjectInputStream in = new ObjectInputStream(communicationSocket.getInputStream());
				// Parses message to an Message object type
				message = (Message) in.readObject();
				// Verifies if message comes from a new client
				checkForSenderInServerList(message.getSender());
				// Verifies if message is a Exit message
				if(message.getMessageText().equals(Constants.EXIT)){
					removeUser(message.getSender());
					message.setMessageText(Constants.CLIENT_LOGOUT);
				}
				// Send message to clients
				sendMessageToClients(message);
				// Closes the socket
				communicationSocket.close();
			}
			
		}
		catch(Exception e){
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
	
	private void sendMessageToClients(Message message) {
		
		recordLog(Constants.MESSAGE_FROM + message.getSender().getUserNickname() + " (" + message.getSender().getUserIp() + ")\n" + message.getMessageText());

		// For each user listed
		for (User user : connectedUsers) {
			// Sends Message
			sendMessage(user, message);
		}

		// Records the message in Log and Chat
		recordLog(Constants.TO_USERS + connectedUsers.toString());
		recordChat(message.toString());
	}

	private void sendMessage(User user, Message message) {
		
		Socket connection = null;
		ObjectOutputStream out = null;

		try {
			// Creates the output stream
			connection = new Socket(user.getUserIp(), user.getUserPort());
			out = new ObjectOutputStream(connection.getOutputStream());
			
			// Writes on the stream
			out.writeObject(message);
			out.flush();
			
			// Closes stream and connection
			out.close();
			connection.close();
		} catch (Exception e) {
			recordLog(Constants.E_SENDING_TO_CLIENTS + message);
		}
		
	}
	
	private void checkForSenderInServerList(User sender){
		if(!getConnectedUsers().contains(sender)){
			getConnectedUsers().add(sender);
			getWindowController().updateOnlineUsersList(getConnectedUsers());
		}
	}
	
	private void removeUser(User userToRemove){
		for(User user : getConnectedUsers()){
			if(user.equals(userToRemove)){
				getConnectedUsers().remove(userToRemove);
				getWindowController().updateOnlineUsersList(getConnectedUsers());
				return;
			}
		}
	}
	
	private void recordLog(String message){
		windowController.getServerFrame().getLogArea().append(message + "\n-----------------------------\n");
	}
	
	private void recordChat(String message){
		windowController.getServerFrame().getChatArea().append(message + "\n");
	}
	
	private ArrayList<User> getConnectedUsers() {
		return connectedUsers;
	}

	private ServerController getWindowController() {
		return windowController;
	}

}
