package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainWindow extends Composite {

    private static MainWindowUiBinder uiBinder = GWT.create(MainWindowUiBinder.class);

    interface MainWindowUiBinder extends UiBinder<Widget, MainWindow> {
    }

    @UiField
    Button compile;

    @UiField
    RichTextArea sourceCode;

    @UiField
    TextBox errorWindow;

    @UiField
    VerticalPanel memoryWindow;

    @UiField
    Button stepButton;

    @UiField
    ListBox registerWindow;

    private final Exe exe = new Exe();

    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        sourceCode.setHTML("mvi b,2</br>mov a,b</br>mov c,b");
        refreshRegisters();
        createMemoryWindowItems();
    }

    private void createMemoryWindowItems() {
        memoryWindow.clear();
        for (int i = 0; i < 20; ++i) {
            TextBox tb = new TextBox();
            memoryWindow.add(tb);
        }
    }

    private void fillMemoryWindow(int start) {
        for (int i = 0; i < memoryWindow.getWidgetCount(); ++i) {
            TextBox tb = (TextBox) memoryWindow.getWidget(i);
            tb.setText("" + exe.getMemory(start + i));
        }
    }

    @UiHandler("compile")
    public void compileHandler(ClickEvent e) {
        String text = sourceCode.getText();
        try {
            exe.compileCode(text, "");
            exe.reset();
            errorWindow.setText("Finished parsing");
            exe.reset();
            refreshRegisters();
            refreshMemory();
        } catch (Exception ex) {
            errorWindow.setText(ex.getMessage());
        }
    }

    private void refreshMemory() {
        fillMemoryWindow(0);
    }

    @UiHandler("stepButton")
    public void stepButtonHandler(ClickEvent e) {
        try {
            exe.step();
        } catch (Exception e1) {
            errorWindow.setText(e1.getMessage());
        } finally {
            refreshRegisters();
            refreshMemory();
        }
    }

    public void refreshRegisters() {
        registerWindow.clear();

        registerWindow.addItem("a = " + toHex(exe.a));
        registerWindow.addItem("b = " + toHex(exe.b));
        registerWindow.addItem("c = " + toHex(exe.c));
        registerWindow.addItem("d = " + toHex(exe.d));
        registerWindow.addItem("e = " + toHex(exe.e));
        registerWindow.addItem("h = " + toHex(exe.h));
        registerWindow.addItem("l = " + toHex(exe.l));
        registerWindow.addItem("ip = " + toHex(exe.ip));
    }

    private String toHex(int i) {
        String str = Integer.toHexString(i);
        if (str.length() == 1) {
            str = "0" + str;
        }
        str = str.toUpperCase();
        return str;
    }
}
