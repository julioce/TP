package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import views.ClientWindow;

import models.Client;
import models.Constants;
import models.Message;
import models.User;

public class ClientController implements ActionListener, WindowListener {
	
	private ClientWindow clientFrame = null;
	private String username = null;
	
	public ClientController(ClientWindow paramClientFrame, String paramUsername) {
		clientFrame = paramClientFrame;
		username = paramUsername;
	}


	@Override
	public void actionPerformed(ActionEvent arg0){
		if(arg0.getActionCommand().equals(Constants.SEND)){
			String message = clientFrame.getMessageArea().getText();

			User sender = new User("127.0.0.1", username);
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

		User sender = new User("127.0.0.1", username);
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
