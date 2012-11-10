package main;

import models.Server;
import views.ServerWindow;
import controllers.ServerController;

public class RunServer {

	public static void main(String[] args) {

		ServerWindow serverFrame = new ServerWindow();
		serverFrame.setUp();

		ServerController windowController = new ServerController(serverFrame);
		
		Server server = new Server(windowController);
		server.start();

	}
}
