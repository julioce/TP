package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import views.ServerWindow;

import models.User;


public class ServerController implements ActionListener
{

	private ServerWindow serverFrame = null;
	
	public ServerController(ServerWindow paramServerFrame) 
	{
		serverFrame = paramServerFrame;
	}

	public void updateOnlineUsersList(List<User> onlineUsers)
	{
		serverFrame.getOnlineUsersJList().setListData(onlineUsers.toArray());
		serverFrame.getOnlineUsersJList().repaint();
		serverFrame.repaint();
	}

	public ServerWindow getServerFrame()
	{
		return serverFrame;
	}


	public void setServerFrame(ServerWindow paramServerFrame) 
	{
		serverFrame = paramServerFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
