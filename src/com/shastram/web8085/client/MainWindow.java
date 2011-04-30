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
    ListBox exeWindow;

    @UiField
    Button stepButton;

    @UiField
    ListBox registerWindow;

    private Exe exe = new Exe();

    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        sourceCode.setHTML("mov a,b</br>sub b</br>add c");
        refreshRegisters();
    }

    @UiHandler("compile")
    public void compileHandler(ClickEvent e) {
        String text = sourceCode.getText();
        try {
            exeWindow.clear();
            exe.compileCode(text);
            errorWindow.setText("Finished parsing");
            while(exe.hasNext()) {
                this.exeWindow.addItem(exe.next());
            }
            exe.resetRegisters();
        } catch(Exception ex) {
            errorWindow.setText(ex.getMessage());
        }
    }

    @UiHandler("stepButton")
    public void stepButtonHandler(ClickEvent e) {
        try {
            exe.step();
        } catch (Exception e1) {
            errorWindow.setText(e1.getMessage());
        } finally {
            refreshRegisters();
        }
    }

    public void refreshRegisters( ) {
        registerWindow.clear();
        registerWindow.addItem("a = " + exe.a);
        registerWindow.addItem("b = " + exe.b);
        registerWindow.addItem("c = " + exe.c);
        registerWindow.addItem("d = " + exe.d);
        registerWindow.addItem("e = " + exe.e);
        registerWindow.addItem("h = " + exe.h);
        registerWindow.addItem("l = " + exe.l);
        registerWindow.addItem("ip = " + exe.ip);
    }
}
