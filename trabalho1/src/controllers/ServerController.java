package controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import views.ServerWindow;

import models.Constants;
import models.Server;
import models.User;

public class ServerController {

	private ServerWindow serverFrame = null;
	
	public ServerController(ServerWindow frame) {
		serverFrame = frame;
	}
	
	public static void main(String[] args) {
		// Creates the View
		ServerWindow serverFrame = new ServerWindow();
		serverFrame.createWindow();
		
		// Sets local IP address
		String ipAddress = Constants.SERVER_IP;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, Constants.E_GETTING_IP);
		}

		ServerController windowController = new ServerController(serverFrame);
		Server server = new Server(windowController, ipAddress);
		// Starts the Server thread
		server.start();

	}

	public void updateOnlineUsersList(List<User> onlineUsers) {
		serverFrame.getOnlineUsersJList().setListData(onlineUsers.toArray());
		serverFrame.getOnlineUsersJList().repaint();
		serverFrame.repaint();
	}

	public ServerWindow getServerFrame() {
		return serverFrame;
	}

	public void setServerFrame(ServerWindow paramServerFrame) {
		serverFrame = paramServerFrame;
	}
	
}
