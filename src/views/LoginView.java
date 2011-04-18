package views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import utils.ServerUtil;


import application.MultiDraw;

@SuppressWarnings("serial")
public class LoginView extends MultiDrawStateView {
	private JTextField login;

	public LoginView(MultiDraw m) {
		super(m);
		login = new JTextField();
	}
	
	protected void setup(){
		JPanel loginPage = new JPanel(new GridLayout(3, 1, 5, 2));
		JPanel loginPane = new JPanel(new FlowLayout());
		JLabel info = new JLabel("Please enter a username to use MultiDraw");
		JLabel label = new JLabel("Username: ");
		JButton button = new JButton("Login");
		info.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(75, 20));
		login.setPreferredSize(new Dimension(100, 20));
		button.setPreferredSize(new Dimension(75, 20));
		button.addActionListener(this);

		loginPane.add(label);
		loginPane.add(login);
		loginPane.add(button);

		loginPage.add(info);
		loginPage.add(loginPane);
		
		// Submit on "enter" key release
		loginPage.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released ENTER"), "submit");
		loginPage.getActionMap().put("submit", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				LoginView.this.actionPerformed(e);
			}
		});
		
		add(loginPage);
		mdFrame.setTitle("Login");
		repaint();
		revalidate();
	}
	
	public String getUsername(){
		return login.getText().trim();
	}
	
	public void actionPerformed(ActionEvent e){
		boolean loggedIn = false;
		try { 
			loggedIn = ServerUtil.getServerInstance().login(getUsername(), InetAddress.getLocalHost().getHostAddress());
			if ( !loggedIn ){
				JOptionPane.showMessageDialog(md, "Username is unavailable.", "Bad Username", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally { 
			ServerUtil.setUserName(getUsername());
			md.sm.transition(this);
		}
	}
}
