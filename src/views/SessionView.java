package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.MultiDraw;

import utils.ServerUtil;

@SuppressWarnings("serial")
public class SessionView extends MultiDrawStateView implements ListSelectionListener{

	public JList sessionList;
	public JList userList;
	private JButton joinSessionBtn;
	private JButton createSessionBtn;

	public SessionView(MultiDraw m) {
		super(m);
	}
	
	protected void setup(){
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
				
				userList = new JList();
			} else {
				userList = new JList();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		JLabel sessionLabel = new JLabel("Sessions");
		JLabel userLabel = new JLabel("Users");

		information.setBorder(new EmptyBorder(5, 15, 5, 15));
		
		createSessionBtn.setPreferredSize(new Dimension(150, 20));
		createSessionBtn.addActionListener(this);
		
		joinSessionBtn.setEnabled(false);
		joinSessionBtn.setPreferredSize(new Dimension(180, 20));
		joinSessionBtn.addActionListener(this);
		
		sessionList.setBorder(new LineBorder(Color.BLACK));
		sessionList.addListSelectionListener(this);
		
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
		
		mdFrame.setTitle("Sessions");
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(createSessionBtn)) {
			try {
				ServerUtil.setShapes(ServerUtil.getServerInstance().connectToSession(null, ServerUtil.getUserName()));
				ServerUtil.setSession(ServerUtil.getUserName());
			} catch(Exception e1) {
			}
		}
		if(e.getSource().equals(joinSessionBtn)) {
			try {
				ServerUtil.setShapes(ServerUtil.getServerInstance().connectToSession((String)sessionList.getSelectedValue(), ServerUtil.getUserName()));
				ServerUtil.setSession((String)sessionList.getSelectedValue());
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		md.sm.transition();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		joinSessionBtn.setEnabled(true);
		try {
			userList.setListData(ServerUtil.getServerInstance().getSession((String)sessionList.getSelectedValue()).getActiveUsers().toArray());
		} catch(Exception e1) {	}
	}
}
