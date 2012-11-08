package br.ufrj.tp.main;

import br.ufrj.tp.controller.ServerWindowController;
import br.ufrj.tp.threads.server.Server;
import br.ufrj.tp.view.ServerFrame;

public class RunServer {

	public static void main(String[] args) {

		ServerFrame serverFrame = new ServerFrame();
		serverFrame.setUp();

		ServerWindowController windowController = new ServerWindowController(serverFrame);
		
		Server server = new Server(windowController);
		server.start();

	}
}
