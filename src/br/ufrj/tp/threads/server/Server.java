package br.ufrj.tp.threads.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import br.ufrj.tp.controller.ServerWindowController;
import br.ufrj.tp.model.Message;
import br.ufrj.tp.model.User;
import br.ufrj.tp.utils.Constants;

public class Server extends Thread {

	private Logger logger = Logger.getLogger(Server.class);
	
	private ServerWindowController windowController = null;
	private ServerSocket serverSocket = null;   
	private Socket communicationSocket = null;  
	
	private ArrayList<User> connectedUsers = new ArrayList<User>();
	
	public Server(ServerWindowController paramController)
	{
		windowController = paramController;
	}
	
	@Override
	public void run() {

		logger.debug("Server running!");
		
		try{
			serverSocket = new ServerSocket(Constants.SERVER_PORT);  
			logger.debug("Server socket opened!");
			
			while(true)
			{
				logger.debug("Waiting for client to connect...");
				communicationSocket = serverSocket.accept();
				Communication conn = new Communication(communicationSocket, this);
				conn.start();
				
			}
			
		}
		catch(BindException e)
		{
			closeSockets();
			System.exit(1);
		}
		catch(IOException e)
		{  
			logger.error("Exception while accepting the connection to a client");
			e.printStackTrace();
		}
		finally
		{
			closeSockets();
		}
	}


	private void closeSockets()
	{
		try
		{
			communicationSocket.close();
			serverSocket.close();
		}
		catch (IOException e)
		{
			logger.error("Exception trying to close the sockets");
			e.printStackTrace();
		}
	}
	
	public void broadcastMessage(Message paramMessage) throws IOException
	{
		logger.debug("Message to broadcast: " + paramMessage.getMessageText());
		logger.debug("From IP: " + paramMessage.getSender().getIpHost());

		logger.debug("Retrieving connected users...");
		logger.debug(connectedUsers);

		for (User user : connectedUsers) 
		{
			logger.debug("For user: " + user.getUsername());
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
	
	public ArrayList<User> getConnectedUsers() 
	{
		return connectedUsers;
	}


	public void setConnectedUsers(ArrayList<User> paramConnectedUsers) 
	{
		connectedUsers = paramConnectedUsers;
	}

	public ServerWindowController getWindowController() 
	{
		return windowController;
	}

	public void setWindowController(ServerWindowController paramWindowController) 
	{
		windowController = paramWindowController;
	}
	
	


}
