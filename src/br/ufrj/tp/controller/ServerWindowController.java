package br.ufrj.tp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import br.ufrj.tp.model.User;
import br.ufrj.tp.view.ServerFrame;

public class ServerWindowController implements ActionListener
{

	private ServerFrame serverFrame = null;
	
	public ServerWindowController(ServerFrame paramServerFrame) 
	{
		serverFrame = paramServerFrame;
	}

	public void updateOnlineUsersList(List<User> onlineUsers)
	{
		serverFrame.getOnlineUsersJList().setListData(onlineUsers.toArray());
		serverFrame.getOnlineUsersJList().repaint();
		serverFrame.repaint();
	}

	public ServerFrame getServerFrame()
	{
		return serverFrame;
	}


	public void setServerFrame(ServerFrame paramServerFrame) 
	{
		serverFrame = paramServerFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
