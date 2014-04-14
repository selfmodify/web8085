package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SaveFileDialog extends DialogBox {

    private static SaveFileDialogUiBinder uiBinder = GWT.create(SaveFileDialogUiBinder.class);

    @UiField
    Button saveButton;

    @UiField
    Button dismissButton;

    @UiField
    TextBox fileName;

    @UiField
    Label status;

    interface SaveFileDialogUiBinder extends UiBinder<Widget, SaveFileDialog> {
    }

    public SaveFileDialog() {
        setWidget(uiBinder.createAndBindUi(this));
    }

    public SaveFileDialog(String filename) {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties("Save File.");
        this.fileName.setText(filename);
    }


    private void setProperties(String title) {
        setText(title);
        setAnimationEnabled(true);
        setAutoHideEnabled(true);
        setModal(true);
        setGlassEnabled(true);
    }

    @UiHandler("saveButton")
    void saveButtonHandler(ClickEvent e) {
        
    }

    @UiHandler("dismissButton")
    void dismissButtonHandler(ClickEvent e) {
        this.hide();
    }

    public void saveAfterAskingFileName(MainWindow mainWindow, String sourceCode) {
        saveWithoutAsking(mainWindow, sourceCode);
    }

    public void saveWithoutAsking(final MainWindow mainWindow, String sourceCode) {
        FileData fileData = new FileData(fileName.getText(), sourceCode);
        status.setText("Saving file '" + fileName.getText() + "' ...");
        this.center();
        MainWindow.rpcService.saveFile(fileData, new AsyncCallback<ServiceResponse>() {
            @Override
            public void onFailure(Throwable caught) {
            }
            @Override
            public void onSuccess(ServiceResponse result) {
                if (result.isLoginRequired()) {
                    mainWindow.startLogin();
                } else {
                    mainWindow.setStatusUpdateLabel(result.getMsg());
                    dismissButtonHandler(null);
                }
            }
        });
    }
}
