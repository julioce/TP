package models;

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
			
			checkForSenderInServerList(message.getSender());
			
			if(message.getMessageText().equals(Constants.EXIT)){
				removeUser(message.getSender());
				message.setMessageText("Left the party");
			}
			
			server.broadcastMessage(message);
			communicationSocket.close();
		} 
		catch (Exception e){
			e.printStackTrace();
		}

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
				server.getConnectedUsers().remove(userToRemove);
				server.getWindowController().updateOnlineUsersList(server.getConnectedUsers());
				return;
			}
		}
	}
}
