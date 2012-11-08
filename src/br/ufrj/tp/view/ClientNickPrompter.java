package br.ufrj.tp.view;

import javax.swing.JOptionPane;

public class ClientNickPrompter
{
	
	private String nickname = "";
	
	public String promptForNick()
	{
		do
		{
			nickname = JOptionPane.showInputDialog("Insert your nickname");
		} while(nickname.equals(""));
		
		return nickname;
		
	}

	public String getNickname() 
	{
		return nickname;
	}

	public void setNickname(String paramNickname) 
	{
		nickname = paramNickname;
	}
	
	

}
