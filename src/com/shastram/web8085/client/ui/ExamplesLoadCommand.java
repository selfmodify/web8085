package com.shastram.web8085.client.ui;

import java.util.HashMap;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public class ExamplesLoadCommand implements Command {

	private static HashMap<String, String> examplesCode = new HashMap<String, String>();
	private final MenuItem item;

	  public ExamplesLoadCommand(MenuItem item) {
	    this.item = item;
	  }
	
	@Override
	public void execute() {
		String code = examplesCode.get(item.getText());
		if (code == null) {
			
		}
	}
}
