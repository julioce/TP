package models;

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
			client.getWindowController().getClientFrame().getChatArea().append(message.toString() + "\n");
			communicationSocket.close();
		} 
		catch (Exception e){
			e.printStackTrace();
		}

	}

}
