package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import application.MultiDraw;
import application.MultiDraw.AppCloser;

import utils.State;

@SuppressWarnings("serial")
public abstract class MultiDrawStateView extends JPanel implements State, ActionListener {
	protected MultiDraw md;
	protected Container mdContentPane; 
	protected JFrame mdFrame;
	
	public MultiDrawStateView(MultiDraw m){
		md = m;
		mdContentPane = m.getContentPane();
		mdFrame = m.frame;
	}
	
	protected abstract void setup();
	
	protected void finalize(){
		mdContentPane.add(this);
		mdFrame.getContentPane().setLayout(new BorderLayout());
		mdFrame.getContentPane().add(mdContentPane, BorderLayout.CENTER);
		mdFrame.addWindowListener(new AppCloser());
		mdFrame.pack();
		mdFrame.setVisible(true);
	}
	
	public void enter(){
		mdContentPane.removeAll();
		mdContentPane.invalidate();
		mdContentPane.validate();
		setup();
		finalize();
	}
	
	public void exit(){	}
}
