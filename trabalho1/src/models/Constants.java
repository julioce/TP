package models;

public class Constants {

	public static final String EXIT = "EXIT";
	public static final String SEND = "SEND_MESSAGE";
	public static final String GREETING = "GREETING";
	public static final String SERVER_IP = "127.0.0.1";
	public static final String CLIENT_IP = "127.0.0.1";
	public static final int SERVER_PORT = 4321;
	public static final int CLIENT_PORT = 12345;
	
	//Server Messages
	public static final String RUNNING = "Server running on ";
	public static final String SOCKET_OPEN = "Server socket opened...";
	public static final String WAITING = "Server waiting for connections...";
	public static final String MESSAGE_FROM = "Message from: ";
	public static final String CONNETED = "Connected";
	public static final String MESSAGES = "Messages";
	public static final String LOG = "Log";
	public static final String SERVER_WINDOW = "Server";
	public static final String TO_USERS = "To users: ";
	public static final String E_CONNECT_TO_CLIENTS = "Error while accepting the connection to a client";
	public static final String E_SENDING_TO_CLIENTS = "Error while sending message to a client ";
	public static final String E_CLOSING_SOCKETS = "Error trying to close the sockets";
	
	//Client Messages
	public static final String ASK_NICKNAME = "What's your nickname?";
	public static final String ASK_SERVER = "What's the server IP Address?";
	public static final String CLIENT_WINDOW = "Chat - ";
	public static final String CLIENT_SEND_BUTTON = "Send";
	public static final String CLIENT_LOGIN = "Is connected to the server";
	public static final String CLIENT_LOGOUT = "Has disconnected from the server";
	public static final String E_CONNECT_TO_SERVER = "Error while accepting the connection to a server";
	public static final String E_GETTING_IP = "Error while getting current IP address";
}
