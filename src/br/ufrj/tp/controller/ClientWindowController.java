package br.ufrj.tp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import br.ufrj.tp.model.Message;
import br.ufrj.tp.model.User;
import br.ufrj.tp.threads.client.Client;
import br.ufrj.tp.utils.Constants;
import br.ufrj.tp.view.ClientFrame;

public class ClientWindowController implements ActionListener, WindowListener
{
	
	private ClientFrame clientFrame = null;
	private String username = null;
	
	public ClientWindowController(ClientFrame paramClientFrame, String paramUsername) 
	{
		clientFrame = paramClientFrame;
		username = paramUsername;
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getActionCommand().equals(Constants.SEND))
		{
			String message = clientFrame.getMessageArea().getText();

			User sender = new User("127.0.0.1", username);
			Message m = new Message(sender, message);
			
			Client.sendMessageToServer(m);
			
			clientFrame.getMessageArea().setText("");
		}
		
	}


	public ClientFrame getClientFrame()
	{
		return clientFrame;
	}


	public void setClientFrame(ClientFrame paramClientFrame) 
	{
		clientFrame = paramClientFrame;
	}


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		String message = Constants.EXIT;

		User sender = new User("127.0.0.1", username);
		Message m = new Message(sender, message);
		
		Client.sendMessageToServer(m);
		
		
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
