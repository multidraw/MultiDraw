package views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controllers.StartUpController;

@SuppressWarnings("serial")
public class LoginView extends JPanel{

	public boolean loggingIn = true;
	public JTextField login = new JTextField();

	

	public LoginView() {
		JPanel loginPage = new JPanel(new GridLayout(3, 1, 5, 2));
		JPanel loginPane = new JPanel(new FlowLayout());
		JLabel info = new JLabel("Please enter a username to use MultiDraw");
		JLabel label = new JLabel("Username: ");
		JButton button = new JButton("Login");
		info.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(75, 20));
		login.setPreferredSize(new Dimension(100, 20));
		button.setPreferredSize(new Dimension(75, 20));
		button.addActionListener(new StartUpController(this));

		loginPane.add(label);
		loginPane.add(login);
		loginPane.add(button);

		loginPage.add(info);
		loginPage.add(loginPane);
		add(loginPage);
		repaint();
		revalidate();
	}
}
