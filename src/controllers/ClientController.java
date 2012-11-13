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
	
	public ClientController(ClientWindow paramClientFrame, String paramipAddress, String paramUsername) {
		clientFrame = paramClientFrame;
		ipAddress = paramipAddress;
		username = paramUsername;
	}
	
	public static void main(String[] args) {

		ClientNicknamePopup clientNickPrompter = new ClientNicknamePopup();
		String ipAddress = Constants.CLIENT_IP;
		String username = clientNickPrompter.askNickname();
		
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, Constants.E_GETTING_IP);
			System.exit(1);
		}
		
		ClientWindow clientFrame = new ClientWindow();
		clientFrame.createWindow(username);

		ClientController windowController = new ClientController(clientFrame, ipAddress, username);
		clientFrame.configureListeners(windowController);
		clientFrame.setupWindowListener(windowController);
		
		User user = new User(ipAddress, username, Constants.CLIENT_PORT);
		Client client = new Client(windowController, username, Constants.CLIENT_PORT);
		client.start();
		
		Message message = new Message(user, Constants.CLIENT_LOGIN);
		Client.sendMessageToServer(message);
	}

	@Override
	public void actionPerformed(ActionEvent arg0){
		if(arg0.getActionCommand().equals(Constants.SEND) && !clientFrame.getMessageArea().getText().isEmpty()){
			String message = clientFrame.getMessageArea().getText();

			User sender = new User(ipAddress, username);
			Message m = new Message(sender, message);
			
			Client.sendMessageToServer(m);
			
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
		String message = Constants.EXIT;

		User sender = new User(ipAddress, username);
		Message m = new Message(sender, message);
		
		Client.sendMessageToServer(m);
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
