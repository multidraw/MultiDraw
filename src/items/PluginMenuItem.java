package items;

import java.awt.event.ActionEvent;

import plugins.PluginWindow;

import views.DrawingCanvasView;

@SuppressWarnings("serial")
public class PluginMenuItem extends FileMenuItem {

	public PluginMenuItem(DrawingCanvasView c) {
		super("Load Plugin", c);
	}

	public void actionPerformed(ActionEvent e) {
		new PluginWindow(canvas);
	}

}
