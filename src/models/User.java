package models;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userIp;
	private int userPort;
	private String userNickname;
	
	public User(String host, String nickname, int port){
		userIp = host;
		userNickname = nickname;
		userPort = port;
	}
	
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public int getUserPort() {
		return userPort;
	}

	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public boolean equals(Object obj) {
		
		if(!(obj instanceof User)){
			return false;
		}
		
		User user = (User) obj;
		
		boolean sameUsername = user.getUserNickname().equals(userNickname);
		boolean sameIP = (user.getUserIp().trim()).equals(userIp.trim());
		
		if(sameUsername && sameIP){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(userNickname + " (" + userIp + ")");
		return stringBuffer.toString();
	}
	
}
