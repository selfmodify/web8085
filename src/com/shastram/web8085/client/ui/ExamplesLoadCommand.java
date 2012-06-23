package com.shastram.web8085.client.ui;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;
import com.shastram.web8085.client.ClientUtils;
import com.shastram.web8085.client.pattern.SignalSlot;

public class ExamplesLoadCommand implements Command {
	protected MenuItem item;
	protected String code;

	  public ExamplesLoadCommand(MenuItem item) {
	    this.item = item;
	  }

	@Override
	public void execute() {
	}
	
	protected void loadRemoteExample(final String name) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				"/simulator/test_cases/" + name);
		builder.setCallback(new RequestCallback() {
			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() == Response.SC_OK) {
					code = response.getText();
					SignalSlot.instance.notifyAbout(SignalSlot.Signals.EXAMPLE_SOURCE_CODE_AVAILABLE,
					        "name", name, "code", code);
				} else {
					ClientUtils.showError(response.getStatusText());
				}
			}
			
			@Override
			public void onError(Request request, Throwable exception) {
				ClientUtils.showError(exception.getMessage());
			}
		});
		try {
			builder.send();
		} catch (RequestException e) {
			ClientUtils.showError(e.getMessage());
		}
	}
}
