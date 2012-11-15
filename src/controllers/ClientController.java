package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import views.ClientNicknamePopup;
import views.ClientWindow;

import models.Client;
import models.Constants;
import models.Message;
import models.User;

public class ClientController implements ActionListener, WindowListener {
	
	private ClientWindow clientFrame = null;
	private String ipAddress = null;
	private String username = null;
	private String serverAddress = null;
	
	public ClientController(ClientWindow frame, String ip, String nickname, String server) {
		clientFrame = frame;
		ipAddress = ip;
		username = nickname;
		serverAddress = server;
	}
	
	public static void main(String[] args) {

		// Gets nickname and server Address pop-up
		ClientNicknamePopup clientNickPrompter = new ClientNicknamePopup();
		String username = clientNickPrompter.askNickname();
		String serverAddress = clientNickPrompter.askServerAddress();
		
		// Sets local IP address
		String ipAddress = Constants.CLIENT_IP;
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, Constants.E_GETTING_IP);
			System.exit(1);
		}
		
		// Creates the View
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.createWindow(username);
		
		ClientController windowController = new ClientController(clientFrame, ipAddress, username, serverAddress);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		// Creates the User
		User user = new User(ipAddress, username, Constants.CLIENT_PORT);
		Client client = new Client(windowController, Constants.CLIENT_PORT);
		// Starts the Thread
		client.start();
		
		// Sends initial message to server
		Message message = new Message(user, Constants.CLIENT_LOGIN);
		Client.sendMessageToServer(serverAddress, message);
	}

	@Override
	public void actionPerformed(ActionEvent arg0){
		if(arg0.getActionCommand().equals(Constants.SEND) && !clientFrame.getMessageArea().getText().isEmpty()){
			// Get text area text
			String messageText = clientFrame.getMessageArea().getText();

			// Set a sender & message
			User sender = new User(ipAddress, username, Constants.CLIENT_PORT);
			Message message = new Message(sender, messageText);
			
			// Sends the message to server
			Client.sendMessageToServer(serverAddress, message);
			
			// Empties the text area
			clientFrame.getMessageArea().setText("");
		}
		
	}

	public ClientWindow getClientFrame() {
		return clientFrame;
	}

	public void setClientFrame(ClientWindow paramClientFrame) {
		clientFrame = paramClientFrame;
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// Sets the default exit message
		String messageText = Constants.EXIT;

		// Set a sender & message
		User sender = new User(ipAddress, username, Constants.CLIENT_PORT);
		Message message = new Message(sender, messageText);
		
		// Sends the message to server
		Client.sendMessageToServer(serverAddress, message);
	}

	@Override
	public void windowActivated(WindowEvent arg0) { }

	@Override
	public void windowClosed(WindowEvent arg0) { }

	@Override
	public void windowDeactivated(WindowEvent arg0) { }

	@Override
	public void windowDeiconified(WindowEvent arg0) { }

	@Override
	public void windowIconified(WindowEvent arg0) { }

	@Override
	public void windowOpened(WindowEvent arg0) { }
	
}
