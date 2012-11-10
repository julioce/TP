package main;

import views.ClientWindow;
import views.ClientNicknamePopup;
import models.Client;
import models.Message;
import models.User;
import controllers.ClientController;

public class RunClient
{

	public static void main(String[] args) {

		ClientNicknamePopup clientNickPrompter = new ClientNicknamePopup();
		String username = clientNickPrompter.promptForNick();
		
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.setUp(username);

		ClientController windowController = new ClientController(clientFrame, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		int port = 1234;
		
		User user = new User("127.0.0.1", username, port);
		Message message = new Message(user, "In the house!");

		
		
		Client client = new Client(windowController, username, port);
		client.start();
		
		Client.sendMessageToServer(message);
	}
	
}
