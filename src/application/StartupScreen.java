package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.MultiDraw.AppCloser;

public class StartupScreen {

	private MultiDraw multiDraw;
	private JFrame frame = new JFrame();

	public StartupScreen(MultiDraw multiDraw) {
		this.multiDraw = multiDraw;
	}
	public String login() {
		JPanel jPane = new JPanel();
		jPane.setLayout(new BoxLayout(jPane, BoxLayout.Y_AXIS));
		JLabel info = new JLabel("Please enter a username to use MultiDraw");
		JLabel label = new JLabel("Username: ");
		JTextField login = new JTextField();
		JButton button = new JButton("Login");
		
		info.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(75, 100));
		login.setPreferredSize(new Dimension(100, 20));
		button.setPreferredSize(new Dimension(75, 20));
		button.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				chooseSession();
			}
		});
		
		JPanel loginPane = new JPanel(new FlowLayout());
		loginPane.add(label);
		loginPane.add(login);
		loginPane.add(button);

		jPane.add(info);
		jPane.add(loginPane);
		
		multiDraw.add(jPane, BorderLayout.CENTER);
		frame.setTitle("Login");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(multiDraw, BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.pack();
		frame.setSize(300, 200);
		frame.setResizable(false);
		frame.setVisible(true);
		while(true);
	}
	
	public void chooseSession() {
		multiDraw.removeAll();
		frame.removeAll();
		JPanel jPane = new JPanel();
		jPane.setLayout(new BoxLayout(jPane, BoxLayout.Y_AXIS));	
		
		JButton createSession = new JButton("Create New Session");
		createSession.setAlignmentX(Component.CENTER_ALIGNMENT);
		createSession.setPreferredSize(new Dimension(200, 20));
		jPane.add(createSession);
		multiDraw.add(jPane, BorderLayout.CENTER);
		
		frame.setTitle("Login");
		frame.getContentPane().add(multiDraw, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(300, 200);
		while(true);

	}
}
