package models;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private User sender;
	private String messageText;

	public Message(User paramSender, String paramMessageText){
		sender = paramSender;
		messageText = paramMessageText;
	}
	
	@Override
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(sender.getUserNickname());
		stringBuffer.append(": ");
		stringBuffer.append(messageText);
		
		return stringBuffer.toString();
	}
	
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

}