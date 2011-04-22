package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.MultiDraw;

@SuppressWarnings("serial")
public class SessionView extends JPanel implements ActionListener, ListSelectionListener{

	public JList sessionList;
	public JList userList;
	private JButton joinSessionBtn;
	private JButton createSessionBtn;
	private MultiDraw md;
	
	public SessionView(MultiDraw m){
		md = m;
	}
	
	private void setup(){
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
		
		JScrollPane sessionSP = null;
		JScrollPane userSP = null;
		
		try {
			sessionList = new JList(md.utilInstance.getServerInstance().getSessions().toArray());
			userList = new JList();
			
			sessionSP = new JScrollPane(sessionList);
			userSP = new JScrollPane(userList);
			sessionSP.setPreferredSize(new Dimension(100, 50));
			userSP.setPreferredSize(new Dimension(100, 50));
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
		sessionsPanel.add(sessionSP, gConstraints);

		usersPanel.add(userLabel);
		usersPanel.add(userSP, gConstraints);
		
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

	public void show(Container contentPane, JFrame frame){
		contentPane.removeAll();
		contentPane.invalidate();
		contentPane.validate();
		
		setup();
		
		contentPane.add(this);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(contentPane, BorderLayout.CENTER);
		frame.setTitle("Sessions");
		frame.pack();
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(createSessionBtn)) {
			try {
				md.utilInstance.setShapes(md.utilInstance.getServerInstance().connectToSession(null, md.utilInstance.getUserName()));
				md.utilInstance.setSession(md.utilInstance.getUserName());
			} catch(Exception e1) {
			}
		}
		if(e.getSource().equals(joinSessionBtn)) {
			try {
				md.utilInstance.setShapes(md.utilInstance.getServerInstance().connectToSession((String)sessionList.getSelectedValue(), md.utilInstance.getUserName()));
				md.utilInstance.setSession((String)sessionList.getSelectedValue());
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		md.showGUIWindow();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		joinSessionBtn.setEnabled(true);
		try {
			userList.setListData(md.utilInstance.getServerInstance().getSession((String)sessionList.getSelectedValue()).getActiveUsers().toArray());
		} catch(Exception e1) {	}
	}
}
