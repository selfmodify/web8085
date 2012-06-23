package com.shastram.web8085.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Simulator implements EntryPoint {
    @Override
    public void onModuleLoad() {
        RootPanel rootPanel = RootPanel.get();

        MainWindow main = new MainWindow();
        rootPanel.add(main);
    }
}
