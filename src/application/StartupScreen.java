package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JTextField;

import application.MultiDraw.AppCloser;

public class StartupScreen {

	private int state = 0;

	public StartupScreen(MultiDraw multiDraw) {
	}
	public String login() {
		state = 1;
		final JFrame frame = new JFrame();
		JPanel jPane = new JPanel();
		jPane.setLayout(new BoxLayout(jPane, BoxLayout.Y_AXIS));
		JLabel info = new JLabel("Please enter a username to use MultiDraw");
		JLabel label = new JLabel("Username: ");
		final JTextField login = new JTextField();
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
				if(login.getText() == null || login.getText() == ""){
					state = 1;
					
					
				}
				//else if(check to see is username is in use)
				else{
					state = 2;
					frame.removeAll();
					frame.dispose();
					frame.setVisible(false);
				}
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
		frame.setLocation(100, 100);
		frame.setSize(300, 200);
		frame.setResizable(false);
		frame.setVisible(true);
		while(state == 1);
		return login.getText(); 
		
		

	}
	
	public String chooseSession() {
		final JFrame frame = new JFrame();

		JPanel jPane = new JPanel();
		jPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JRadioButton createSession = new JRadioButton("Create New Session");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 0;
		c.gridwidth = 5;
		jPane.add(createSession, c);

		JRadioButton joinSession = new JRadioButton("Join Session");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.1;
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 5;
		jPane.add(joinSession, c);

		JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL);
		vbar.setUnitIncrement(2);
		vbar.setBlockIncrement(1);
		vbar.addAdjustmentListener(new AdjustmentListener(){

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				//slides the list of sessions up and down
			}
			
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.1;
		c.gridy = 2;
		c.gridx = 5;
		c.weightx = 0.05;
		c.gridwidth = 1;
		c.ipady = 100;
		jPane.add(vbar, c);

		JTextField sessions = new JTextField();
		sessions.setBackground(Color.WHITE);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.1;
		c.gridy = 2;
		c.gridx = 0;
		c.weightx = 0.95;
		c.gridwidth = 4;
		c.ipady = 100;
		jPane.add(sessions, c);

		JButton sessionChosen = new JButton("Select");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		c.gridy = 3;
		c.gridx = 0;
		c.weightx = 0.25;
		c.ipady = 0;
		c.gridwidth = 0;
		
		jPane.add(sessionChosen, c);
		
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
		frame.addWindowListener(new AppCloser());
		frame.validate();
		frame.setLocation(100, 100);
		frame.setSize(250, 250);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new AppCloser());

		while(state == 2);
		return "session name";
	}
}
