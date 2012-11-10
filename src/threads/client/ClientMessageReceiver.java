package threads.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import models.Message;

import org.apache.log4j.Logger;


public class ClientMessageReceiver extends Thread {

	private Logger logger = Logger.getLogger(ClientMessageReceiver.class);

	private Client client = null;
	private Socket communicationSocket = null;
	private Message message = null;

	public ClientMessageReceiver(Socket paramCommunicationSocket, Client paramClient) 
	{
		communicationSocket = paramCommunicationSocket;
		client = paramClient;
	}

	@Override
	public void run() 
	{
		ObjectInputStream in;

		try {
			
			in = new ObjectInputStream(communicationSocket.getInputStream());

			message = (Message) in.readObject();
			logger.debug("[" + client.getUsername() + "] receiving: " + message.getMessageText() + " From " + message.getSender().getUsername());
			
			client.getWindowController().getClientFrame().getChatArea().append(message.toString() + "\n");

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

	

}
