package views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import application.MultiDraw;

@SuppressWarnings("serial")
public class LoginView extends JPanel implements ActionListener{
	private JTextField login;
	private MultiDraw md;
	
	public LoginView(MultiDraw m) {
		md = m;
		login = new JTextField();
	}
	
	private void setup(){
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
		repaint();
		revalidate();
	}
	
	public void show(Container contentPane, JFrame frame){
		contentPane.removeAll();
		contentPane.invalidate();
		contentPane.validate();
		
		setup();
		
		contentPane.add(this);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.setTitle("Login");
		frame.pack();
		frame.setVisible(true); 
	}
	
	public String getUsername(){
		return login.getText().trim();
	}
	
	public void actionPerformed(ActionEvent e){
		boolean loggedIn = false;
		try { 
			loggedIn = md.utilInstance.getServerInstance().login(md.utilInstance.getClient(), getUsername());
			if ( loggedIn ){
				md.showSessionsWindow();
			} else { 
				JOptionPane.showMessageDialog(this, "Username is unavailable.", "Bad Username", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			md.serverDown = true;
			WindowEvent wev = new WindowEvent(md.frame, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		} finally {
			md.utilInstance.setUserName(getUsername());
		}
	}
}
