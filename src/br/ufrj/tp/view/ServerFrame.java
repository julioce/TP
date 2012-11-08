package br.ufrj.tp.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ServerFrame extends JFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList onlineUsersJList = null;
	private JTextArea chatArea = null;

	
	public ServerFrame() {
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		onlineUsersJList = new JList();
		onlineUsersJList.setBounds(375, 37, 149, 438);
		onlineUsersJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(onlineUsersJList);
		
		JLabel lblOnlineUsers = new JLabel("Online Users");
		lblOnlineUsers.setBounds(416, 11, 108, 14);
		contentPane.add(lblOnlineUsers);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 37, 344, 438);
		contentPane.add(scrollPane);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		scrollPane.setViewportView(chatArea);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setBounds(10, 11, 60, 14);
		contentPane.add(lblChat);
	}
	
	public void setUp()
	{
		setTitle("Server Chat");
		setBounds(100, 100, 550, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JList getOnlineUsersJList() 
	{
		return onlineUsersJList;
	}

	public void setOnlineUsersJList(JList paramOnlineUsersJList)
	{
		onlineUsersJList = paramOnlineUsersJList;
	}

	public JTextArea getChatArea() 
	{
		return chatArea;
	}

	public void setChatArea(JTextArea paramChatArea) 
	{
		this.chatArea = paramChatArea;
	}
	
	
	
	
	
	
}