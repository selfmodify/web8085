package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DialogWidget extends DialogBox {

    private static DialogWidgetUiBinder uiBinder = GWT.create(DialogWidgetUiBinder.class);

    interface DialogWidgetUiBinder extends UiBinder<Widget, DialogWidget> {
    }

    @UiField
    Label msg;

    @UiField
    Button dismissButton;

    public DialogWidget() {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties("","No Error");
    }

    public DialogWidget(String title, String text) {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties(title, text);
    }

    private void setProperties(String title, String text) {
        setText(title);
        msg.setText(text);
        setAnimationEnabled(true);
        setAutoHideEnabled(true);
        setModal(true);
        setGlassEnabled(true);
    }

    @UiHandler("dismissButton")
    void dismissButtonHandler(ClickEvent e) {
        this.hide();
    }
}
