package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class HelpWindow extends Composite {

    private static HelpWindowUiBinder uiBinder = GWT
            .create(HelpWindowUiBinder.class);

    interface HelpWindowUiBinder extends UiBinder<Widget, HelpWindow> {
    }

    public HelpWindow() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
