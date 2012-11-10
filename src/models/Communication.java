package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class Communication extends Thread {

	private Server server = null;
	private Socket communicationSocket = null;
	private Message message = null;

	public Communication(Socket paramCommunicationSocket, Server paramServer) {
		communicationSocket = paramCommunicationSocket;
		server = paramServer;
	}

	@Override
	public void run() {

		ObjectInputStream in;

		try {
			in = new ObjectInputStream(communicationSocket.getInputStream());

			message = (Message) in.readObject();
			
			boolean hasCommands = checkMessageForCommands(message);
			
			if(!hasCommands){
				checkForSenderInServerList(message.getSender());
				server.broadcastMessage(message);
			}

			communicationSocket.close();
		} 
		catch (IOException e){
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e){
			e.printStackTrace();
		}

	}

	private boolean checkMessageForCommands(Message paramMessage) {
		if(paramMessage.getMessageText().equals(Constants.EXIT)){
			removeUser(paramMessage.getSender());
			return true;
		}
		
		return false;
	}
	
	private void checkForSenderInServerList(User sender){
		if(!server.getConnectedUsers().contains(sender)){
			server.getConnectedUsers().add(sender);
			server.getWindowController().updateOnlineUsersList(server.getConnectedUsers());
		}
	}
	
	private void removeUser(User userToRemove){
		for(User user : server.getConnectedUsers()){
			if(user.equals(userToRemove)){
				Message message = new Message(userToRemove, "Left the party...");
				try {
					server.broadcastMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
				server.getConnectedUsers().remove(userToRemove);
				server.getWindowController().updateOnlineUsersList(server.getConnectedUsers());
				return;
			}
		}
		
		//recordLog("User not found, therefore not removed: " + userToRemove);
	}
}
