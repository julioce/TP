package views;

import javax.swing.JOptionPane;

public class ClientNicknamePopup {
	
	private String nickname = "";
	
	public String promptForNick(){
		while(nickname.equals("")){
			nickname = JOptionPane.showInputDialog("Insert your nickname");
		}
		
		return nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String paramNickname) {
		nickname = paramNickname;
	}

}
