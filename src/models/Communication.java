package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import main.Constants;

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

			System.out.println("Message received: " + message.getMessageText());
			System.out.println("From: " + message.getSender().getUsername());

			boolean hasCommands = checkMessageForCommands(message);
			
			if(!hasCommands){
				checkForSenderInServerList(message.getSender());
				server.broadcastMessage(message);
			}

			communicationSocket.close();
		} 
		catch (IOException e){
			System.out.println("IOException caught when running a Connection");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e){
			System.out.println("Couldn't read object in ObjectInputStream");
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
			System.out.println(sender + " was not in list. Adding.");
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
		
		System.out.println("User not found, therefore not removed: " + userToRemove);
	}
}
