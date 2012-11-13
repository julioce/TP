package views;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClientNicknamePopup {
	
	private String nickname = "";
	
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
			nickname = JOptionPane.showInputDialog("What's your nickname?");
		}
		
		return nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
