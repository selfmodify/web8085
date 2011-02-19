package com.mycompany.project.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Simulator implements EntryPoint {
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();

		MainWindow main = new MainWindow();
		rootPanel.add(main);
	}
}
