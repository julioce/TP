package main;

import threads.server.Server;
import views.ServerWindow;
import controllers.ServerWindowController;

public class RunServer {

	public static void main(String[] args) {

		ServerWindow serverFrame = new ServerWindow();
		serverFrame.setUp();

		ServerWindowController windowController = new ServerWindowController(serverFrame);
		
		Server server = new Server(windowController);
		server.start();

	}
}
