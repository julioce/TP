package br.ufrj.tp.threads.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.ufrj.tp.model.Message;
import br.ufrj.tp.model.User;
import br.ufrj.tp.utils.Constants;

public class Communication extends Thread {

	private Logger logger = Logger.getLogger(Communication.class);

	private Server server = null;
	private Socket communicationSocket = null;
	private Message message = null;

	public Communication(Socket paramCommunicationSocket, Server paramServer) 
	{
		communicationSocket = paramCommunicationSocket;
		server = paramServer;
	}

	@Override
	public void run() 
	{

		ObjectInputStream in;

		try {
			in = new ObjectInputStream(communicationSocket.getInputStream());

			message = (Message) in.readObject();

			logger.debug("Message received: " + message.getMessageText());
			logger.debug("From: " + message.getSender().getUsername());

			boolean hasCommands = checkMessageForCommands(message);
			
			if(!hasCommands)
			{
				checkForSenderInServerList(message.getSender());
				server.broadcastMessage(message);
			}

			communicationSocket.close();
		} 
		catch (IOException e)
		{
			logger.error("IOException caught when running a Connection");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			logger.error("Couldn't read object in ObjectInputStream");
			e.printStackTrace();
		}

	}

	private boolean checkMessageForCommands(Message paramMessage)
	{
		if(paramMessage.getMessageText().equals(Constants.EXIT))
		{
			removeUser(paramMessage.getSender());
			return true;
		}
		
		return false;
	}
	
	private void checkForSenderInServerList(User sender)
	{
		if(!server.getConnectedUsers().contains(sender))
		{
			logger.debug(sender + " was not in list. Adding.");
			server.getConnectedUsers().add(sender);
			server.getWindowController().updateOnlineUsersList(server.getConnectedUsers());
		}
	}
	
	private void removeUser(User userToRemove)
	{
		for(User user : server.getConnectedUsers())
		{
			if(user.equals(userToRemove))
			{
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
		
		logger.debug("User not found, therefore not removed: " + userToRemove);
	}
}
