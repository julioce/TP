package models;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ipHost;
	private int userPort = 0;
	private String username;
	
	public User(String paramIpHost, String paramUsername){
		ipHost = paramIpHost;
		username = paramUsername;
	}

	public User(String paramIpHost, String paramUsername, int paramUserPort){
		ipHost = paramIpHost;
		username = paramUsername;
		userPort = paramUserPort;
	}
	
	public String getIpHost() {
		return ipHost;
	}

	public void setIpHost(String paramIpHost) {
		ipHost = paramIpHost;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String paramUsername) {
		username = paramUsername;
	}
	
	public int getUserPort() {
		return userPort;
	}

	public void setUserPort(int paramUserPort) {
		userPort = paramUserPort;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof User)){
			return false;
		}
		
		User user = (User) obj;
		
		boolean sameUsername = user.getUsername().equals(username);
		boolean sameIP = (user.getIpHost().trim()).equals(ipHost.trim());
		
		if(sameUsername && sameIP){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(username);
		stringBuffer.append(" (");
		stringBuffer.append(ipHost);
		stringBuffer.append(")");
		
		return stringBuffer.toString();
	}
	
}
