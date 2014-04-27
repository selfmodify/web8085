package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListFilesDialog extends DialogBox {

    private static ListFilesDialogUiBinder uiBinder = GWT.create(ListFilesDialogUiBinder.class);

    interface ListFilesDialogUiBinder extends UiBinder<Widget, ListFilesDialog> {
    }

    @UiField
    Button dismissButton;

    @UiField
    VerticalPanel listOfOpenFilesPanel;

    public ListFilesDialog() {
        setWidget(uiBinder.createAndBindUi(this));
        setProperties("Open File:");
    }

    private void setProperties(String title) {
        setText(title);
        setAnimationEnabled(true);
        setAutoHideEnabled(false);
        setModal(true);
        setGlassEnabled(true);
    }

    @UiHandler("dismissButton")
    public void dismissButtonHandler(ClickEvent e) {
        this.hide();
    }

    public void showDialog(ServiceResponse result) {
        for(int i=0; i < result.getFileList().size(); ++i) {
            final FileInfo f = result.getFileList().get(i);
            ListFileSingleItem item = new ListFileSingleItem();
            item.setFileInfo(f);
            item.addStyleName(i % 2 == 0 ?
                    Style.style.css.openFildDialogInnerPanelAzure() :
                        Style.style.css.openFildDialogInnerPanelNormal());
            listOfOpenFilesPanel.add(item);
            item.openFileButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    openFileButtonHandler(f);
                }
            });
        }
        this.center();
    }

    public void openFileButtonHandler(FileInfo fileInfo) {
        final ListFilesDialog dialog = this;
        MainWindow.rpcService.openFile(fileInfo, new AsyncCallback<ServiceResponse>() {
            @Override
            public void onSuccess(ServiceResponse result) {
                if (result.getFileData() != null) {
                    MainWindow.INSTANCE.setFileData(result.getFileData());
                    dialog.hide();
                }
            }
            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }
}
