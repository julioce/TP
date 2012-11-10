package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientMessageReceiver extends Thread {

	private Client client = null;
	private Socket communicationSocket = null;
	private Message message = null;

	public ClientMessageReceiver(Socket paramCommunicationSocket, Client paramClient) {
		communicationSocket = paramCommunicationSocket;
		client = paramClient;
	}

	@Override
	public void run() {
		ObjectInputStream in;

		try {
			
			in = new ObjectInputStream(communicationSocket.getInputStream());

			message = (Message) in.readObject();
			System.out.println("[" + client.getUsername() + "] receiving: " + message.getMessageText() + " From " + message.getSender().getUsername());
			
			client.getWindowController().getClientFrame().getChatArea().append(message.toString() + "\n");

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

}
