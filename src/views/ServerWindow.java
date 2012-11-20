package views;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import models.Constants;

public class ServerWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JList onlineUsersJList = null;
	private JTextArea chatArea = null;
	private JTextArea logArea = null;
	
	public ServerWindow() {
		String nativeLF = UIManager.getSystemLookAndFeelClassName();
		
		try {
			UIManager.setLookAndFeel(nativeLF);
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		onlineUsersJList = new JList();
		onlineUsersJList.setBounds(375, 37, 149, 473);
		onlineUsersJList.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPanel.add(onlineUsersJList);
		
		JLabel lblOnlineUsers = new JLabel(Constants.CONNETED);
		lblOnlineUsers.setBounds(410, 11, 108, 18);
		contentPanel.add(lblOnlineUsers);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 37, 344, 238);
		contentPanel.add(scrollPane);
		
		JScrollPane logPanel = new JScrollPane();
		logPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		logPanel.setBounds(10, 310, 344, 200);
		contentPanel.add(logPanel);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		scrollPane.setViewportView(chatArea);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logPanel.setViewportView(logArea);
		
		JLabel labelChat = new JLabel(Constants.MESSAGES);
		labelChat.setBounds(10, 11, 120, 18);
		contentPanel.add(labelChat);
		
		JLabel labelLog = new JLabel(Constants.LOG);
		labelLog.setBounds(10, 285, 60, 18);
		contentPanel.add(labelLog);
	}
	
	public void createWindow(){
		setTitle(Constants.SERVER_WINDOW);
		setBounds(100, 100, 550, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public JList getOnlineUsersJList() {
		return onlineUsersJList;
	}

	public void setOnlineUsersJList(JList paramOnlineUsersJList){
		onlineUsersJList = paramOnlineUsersJList;
	}

	public JTextArea getChatArea() {
		return chatArea;
	}

	public void setChatArea(JTextArea chatArea) {
		this.chatArea = chatArea;
	}
	
	public JTextArea getLogArea() {
		return logArea;
	}

	public void setLogArea(JTextArea logArea) {
		this.logArea = logArea;
	}
	
}