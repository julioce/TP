package views;

import javax.swing.JOptionPane;

public class ClientNicknamePopup {
	
	private String nickname = "";
	
	public String askNickname(){
		while(nickname.isEmpty()){
			nickname = JOptionPane.showInputDialog("Insert your nickname");
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
