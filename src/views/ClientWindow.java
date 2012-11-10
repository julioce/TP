package views;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import models.Constants;

public class ClientWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JButton sendButton = null;
	
	private JTextArea messageArea = null;
	private JTextArea chatArea = null;
	
	public ClientWindow() {
		
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		sendButton = new JButton("Send");
		sendButton.setBounds(365, 352, 89, 57);
		sendButton.setActionCommand(Constants.SEND);
		contentPanel.add(sendButton);
		
		JScrollPane chatScroll = new JScrollPane();
		chatScroll.setBounds(10, 11, 327, 288);
		chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(chatScroll);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatScroll.setViewportView(chatArea);
		
		JScrollPane messageScroll = new JScrollPane();
		messageScroll.setBounds(10, 351, 327, 58);
		messageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		messageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(messageScroll);
		
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageScroll.setViewportView(messageArea);
		
	}

	public void setUp(String title) {
		setTitle("Window chat - " + title);
		setBounds(100, 100, 480, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void configureListeners(ActionListener listener) {
		sendButton.addActionListener(listener);
	}

	public void setupWindowListener(WindowListener listener) {
		this.addWindowListener(listener);
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}

	public void setChatArea(JTextArea chatArea) {
		this.chatArea = chatArea;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}
	
}