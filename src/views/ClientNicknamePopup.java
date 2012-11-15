package views;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import models.Constants;

public class ClientNicknamePopup {
	
	private String nickname = "";
	private String serverAddress = "";
	
	public ClientNicknamePopup(){
		String nativeLF = UIManager.getSystemLookAndFeelClassName();
		
		try {
			UIManager.setLookAndFeel(nativeLF);
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
	}
	
	public String askNickname(){
		while(nickname.isEmpty()){
			nickname = JOptionPane.showInputDialog(Constants.ASK_NICKNAME);
		}
		
		return nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String askServerAddress() {
		while(serverAddress.isEmpty()){
			serverAddress = JOptionPane.showInputDialog(Constants.ASK_SERVER);
		}
		
		return serverAddress;
	}

}
