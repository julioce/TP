package models;

public class Constants {

	public static final String EXIT = "EXIT";
	public static final String SEND = "SEND_MESSAGE";
	public static final String GREETING = "GREETING";
	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 4321;
	
	//Server Messages
	public static final String WAITING = "Waiting for client connection...";
	public static final String CONNECT_TO_CLIENTS = "Exception while accepting the connection to a client";
	public static final String CLOSING_SOCKETS = "Exception trying to close the sockets";
	public static final String MESSAGE_FROM = "Message from: ";
	public static final String TO_USERS = "To users: ";
	
	//Client Messages
	public static final String CLIENT_LOGIN = "In the house!";
	public static final String CLIENT_LOGOUT = "Left the party";
	public static final String CONNECT_TO_SERVER = "Exception while accepting the connection to a server";
}
