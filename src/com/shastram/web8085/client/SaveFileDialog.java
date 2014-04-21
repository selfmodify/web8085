package com.shastram.web8085.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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
    TextBox fileNameTextbox;

    @UiField
    Label status;

    @UiField
    CheckBox overwriteExistingFile;

    private MainWindow mainWindow;

    private Date dateCreated;

    interface SaveFileDialogUiBinder extends UiBinder<Widget, SaveFileDialog> {
    }

    public SaveFileDialog() {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties("Save File.");
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
        // Local save button in the dialog box.
        FileData fileData = new FileData(fileNameTextbox.getText(), mainWindow.getSourceCode(), dateCreated);
        fileData.setOverwriteExisting(overwriteExistingFile.getValue());
        String msg = "Saving file '" + fileNameTextbox.getText() + "' ...";
        mainWindow.setStatusUpdateLabel(msg);
        setStatus(msg);
        final SaveFileDialog dialog = this;
        MainWindow.rpcService.saveFile(fileData, new AsyncCallback<ServiceResponse>() {
            @Override
            public void onFailure(Throwable caught) {
            }
            @Override
            public void onSuccess(ServiceResponse result) {
                if (result.isLoginRequired()) {
                    mainWindow.startLogin();
                } else if (result.wouldHaveOverrittenFile()) {
                    dialog.setStatus(result.getMsg());
                    dialog.center();
                } else {
                    mainWindow.updateSavedFileInfo(result.getFileInfo());
                    mainWindow.setStatusUpdateLabel(result.getMsg());
                    dismissButtonHandler(null);
                }
            }
        });
    }

    @UiHandler("dismissButton")
    void dismissButtonHandler(ClickEvent e) {
        this.hide();
    }

    public void saveAfterAskingFileName(MainWindow mainWindow) {
        this.dateCreated = mainWindow.getDateCreatedOfFile();
        this.mainWindow = mainWindow;
        status.setText("Specify a file name.");
        center();
        fileNameTextbox.selectAll();
        fileNameTextbox.setFocus(true);
    }

    private void saveWithoutAsking(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        saveButtonHandler(null);
    }

    protected void setStatus(String msg) {
        status.setText(msg);
    }

    public void doSave(MainWindow mainWindow) {
        this.dateCreated = mainWindow.getDateCreatedOfFile();
        String fileName = mainWindow.getFileName();
        fileNameTextbox.setText(fileName);
        if (fileName == null || dateCreated == null) {
            saveAfterAskingFileName(mainWindow);
        } else {
            saveWithoutAsking(mainWindow);
        }
    }
}
