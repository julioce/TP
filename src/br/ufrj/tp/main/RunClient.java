package br.ufrj.tp.main;

import br.ufrj.tp.controller.ClientWindowController;
import br.ufrj.tp.model.Message;
import br.ufrj.tp.model.User;
import br.ufrj.tp.threads.client.Client;
import br.ufrj.tp.utils.RandomPortGenerator;
import br.ufrj.tp.view.ClientFrame;
import br.ufrj.tp.view.ClientNickPrompter;

public class RunClient
{

	public static void main(String[] args) {

		ClientNickPrompter clientNickPrompter = new ClientNickPrompter();
		String username = clientNickPrompter.promptForNick();
		
		ClientFrame clientFrame = new ClientFrame();
		clientFrame.setUp(username);

		ClientWindowController windowController = new ClientWindowController(clientFrame, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		int randomPort = RandomPortGenerator.generatePort();
		
		User user = new User("127.0.0.1", username, randomPort);
		Message message = new Message(user, "In the house!");

		
		
		Client client = new Client(windowController, username, randomPort);
		client.start();
		
		Client.sendMessageToServer(message);
	}
	
}
