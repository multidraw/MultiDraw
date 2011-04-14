package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import application.MultiDraw.AppCloser;

public class StartupScreen {

	private int state = 0;

	public StartupScreen(MultiDraw multiDraw) {
	}
	public void login() {
		state = 1;
		final JFrame frame = new JFrame();
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
				state = 2;
				frame.removeAll();
				frame.dispose();
				frame.setVisible(false);
			}
		});
		
		JPanel loginPane = new JPanel(new FlowLayout());
		loginPane.add(label);
		loginPane.add(login);
		loginPane.add(button);

		jPane.add(info);
		jPane.add(loginPane);
		
		frame.setTitle("Login");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(jPane, BorderLayout.CENTER);
		frame.addWindowListener(new AppCloser());
		frame.setSize(300, 200);
		frame.setResizable(false);
		frame.setVisible(true);
		while(state == 1);
		
		

	}
	
	public void chooseSession() {
		final JFrame frame = new JFrame();

		JPanel jPane = new JPanel();
		jPane.setLayout(new BoxLayout(jPane, BoxLayout.Y_AXIS));	
		
		JRadioButton createSession = new JRadioButton("Create New Session");
		createSession.setAlignmentX(Component.CENTER_ALIGNMENT);
		createSession.setPreferredSize(new Dimension(200, 20));
		JRadioButton joinSession = new JRadioButton("Join Session");
		joinSession.setAlignmentX(Component.CENTER_ALIGNMENT);
		joinSession.setPreferredSize(new Dimension(200, 20));
		JTextField sessions = new JTextField();
		sessions.setPreferredSize(new Dimension(200,100));
		sessions.setEditable(false);
		JButton sessionChosen = new JButton("Select");

		jPane.add(createSession);
		jPane.add(joinSession);
		jPane.add(sessions);
		jPane.add(sessionChosen);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(createSession);
		bg.add(joinSession);
		
		sessionChosen.addMouseListener(new MouseListener() {
			
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
				state = 3;
				frame.removeAll();
				frame.dispose();
				frame.setVisible(false);

			}
		});
		
		
		frame.setTitle("Host/Join a Session");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(jPane, BorderLayout.CENTER);
		frame.validate();
		frame.setLocation(400, 400);
		frame.setSize(300, 300);
		frame.setVisible(true);
		while(state == 2);

	}
}
