package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class OpenFilesDialog extends DialogBox {

    private static OpenFilesDialogUiBinder uiBinder = GWT.create(OpenFilesDialogUiBinder.class);

    interface OpenFilesDialogUiBinder extends UiBinder<Widget, OpenFilesDialog> {
    }

    @UiField
    Button dismissButton;

    @UiField
    VerticalPanel listOfOpenFilesPanel;

    public OpenFilesDialog() {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties("Open File:");
    }

    private void setProperties(String title) {
        setText(title);
        setAnimationEnabled(true);
        setAutoHideEnabled(true);
        setModal(true);
        setGlassEnabled(true);
    }

    @UiHandler("dismissButton")
    public void dismissButtonHandler(ClickEvent e) {
        this.hide();
    }

    public void showDialog(ServiceResponse result) {
        for(int i=0; i < result.getFileList().size(); ++i) {
            FileInfo f = result.getFileList().get(i);
            ListFileSingleItem item = new ListFileSingleItem();
            item.setFileInfo(f);
            item.addStyleName(i % 2 == 0 ?
                    Style.style.css.openFildDialogInnerPanelAzure() :
                        Style.style.css.openFildDialogInnerPanelNormal());
            listOfOpenFilesPanel.add(item);
        }
        this.center();
    }
}
