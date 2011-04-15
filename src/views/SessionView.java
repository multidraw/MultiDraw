package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import utils.ServerUtil;

import controllers.StartUpController;

@SuppressWarnings("serial")
public class SessionView extends JPanel{

	public JList sessionList;
	public JList userList;
	public JButton joinSessionBtn;
	public JButton createSessionBtn;
	public boolean sessionSelected = false;

	public SessionView() {
		StartUpController controller = new StartUpController(this);
		FlowLayout fLayout = new FlowLayout(FlowLayout.CENTER, 40, 4); 
		GridBagLayout gBag = new GridBagLayout();
		GridBagConstraints gConstraints = new GridBagConstraints();
		//gBag.
		setLayout(gBag);
		
		JPanel joinSession = new JPanel(fLayout);
		JPanel createSession = new JPanel(fLayout);
		JPanel sessionsAndUsers = new JPanel(fLayout);
		JPanel sessionsPanel = new JPanel(gBag);
		JPanel usersPanel = new JPanel(gBag);
		
		
		JLabel information = new JLabel(
				"Please either create a new session or join an open one.");
		createSessionBtn = new JButton("Create New Session");
		joinSessionBtn = new JButton("Join the selected session");
		
		try {
			sessionList = new JList(ServerUtil.getServerInstance().getSessions().toArray());
			if(sessionList.getModel().getSize() != 0) {
				sessionList.setSelectedIndex(0);
				userList = new JList(ServerUtil.getServerInstance().getSession((String)sessionList.getSelectedValue()).getActiveUsers().toArray());
			} else {
				userList = new JList();
			}
		} catch(Exception e) {
			
		}
		JLabel sessionLabel = new JLabel("Sessions");
		JLabel userLabel = new JLabel("Users");

		information.setBorder(new EmptyBorder(5, 15, 5, 15));
		
		createSessionBtn.setPreferredSize(new Dimension(150, 20));
		createSessionBtn.addActionListener(controller);
		
		joinSessionBtn.setEnabled(false);
		joinSessionBtn.setPreferredSize(new Dimension(180, 20));
		joinSessionBtn.addActionListener(controller);
		
		sessionList.setBorder(new LineBorder(Color.BLACK));
		sessionList.addListSelectionListener(controller);
		
		userList.setBorder(new LineBorder(Color.BLACK));
		userList.setEnabled(false);

		
		createSession.add(createSessionBtn);
		joinSession.add(joinSessionBtn);
		
		gConstraints.gridy = 1;
		
		sessionsPanel.add(sessionLabel);
		sessionsPanel.add(sessionList, gConstraints);

		usersPanel.add(userLabel);
		usersPanel.add(userList, gConstraints);
		
		sessionsAndUsers.add(sessionsPanel);
		sessionsAndUsers.add(usersPanel);

		add(information);
		gConstraints.anchor = GridBagConstraints.CENTER;

		add(createSession, gConstraints);
		gConstraints.gridy = 2;

		add(joinSession, gConstraints);
		gConstraints.gridy = 3;

		add(sessionsAndUsers, gConstraints);

	}
}
