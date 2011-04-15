package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utils.ServerUtil;
import views.LoginView;
import views.SessionView;

public class StartUpController implements ActionListener, ListSelectionListener{

	private LoginView lView = null;
	private SessionView sView = null;
	
	public StartUpController(LoginView loginView) {
		this.lView = loginView;
	}

	public StartUpController(SessionView sessionView) {
		this.sView = sessionView;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(lView != null) {
			try {
				lView.loggingIn = !ServerUtil.getServerInstance().login(lView.login.getText(), InetAddress.getLocalHost().getHostAddress());
				ServerUtil.setUserName(lView.login.getText());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				lView.loggingIn = true;
				e.printStackTrace();
			}
			if(!lView.loggingIn) {
				ServerUtil.setUserName(lView.login.getText());
			}
		}
		
		if(sView != null) {
			if(arg0.getSource().equals(sView.createSessionBtn)) {
				try {
					ServerUtil.setShapes(ServerUtil.getServerInstance().connectToSession(null, ServerUtil.getUserName()));
					ServerUtil.setSession(ServerUtil.getUserName());
					sView.sessionSelected = true;
				} catch(Exception e) {
					sView.sessionSelected = false;
				}
			}
			if(arg0.getSource().equals(sView.joinSessionBtn)) {
				try {
					ServerUtil.setShapes(ServerUtil.getServerInstance().connectToSession((String) sView.sessionList.getSelectedValue(), ServerUtil.getUserName()));
					ServerUtil.setSession((String) sView.sessionList.getSelectedValue());
					sView.sessionSelected = true;
				} catch(Exception e) {
					sView.sessionSelected = false;
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(sView != null) {
			sView.joinSessionBtn.setEnabled(true);
			try {
				sView.userList.setListData(ServerUtil.getServerInstance().getSession((String)sView.sessionList.getSelectedValue()).getActiveUsers().toArray());
			} catch(Exception e) {
				
			}
		}
		
	}

}
