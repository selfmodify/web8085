package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ListFileSingleItem extends Composite {

    private static ListFileSingleItemUiBinder uiBinder = GWT.create(ListFileSingleItemUiBinder.class);

    interface ListFileSingleItemUiBinder extends UiBinder<Widget, ListFileSingleItem> {
    }

    @UiField
    Button openFileButton;

    @UiField
    Button deleteFileButton;

    @UiField
    Label fileName;

    @UiField
    Label lastModified;

    private FileInfo fileInfo;

    public ListFileSingleItem() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        fileName.setText(fileInfo.getFileName());
        lastModified.setText(fileInfo.getDateUpdated().toString());
    }
}
