package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class OpenFilesDialog extends DialogBox {

    private static OpenFilesDialogUiBinder uiBinder = GWT.create(OpenFilesDialogUiBinder.class);

    interface OpenFilesDialogUiBinder extends UiBinder<Widget, OpenFilesDialog> {
    }

    public OpenFilesDialog() {
        setWidget(uiBinder.createAndBindUi(this));
    }
}
