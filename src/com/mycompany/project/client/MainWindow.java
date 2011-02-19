package com.mycompany.project.client;

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
    
    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        sourceCode.setHTML("mov a,b</br>sub b</br>add c");
    }

    @UiHandler("compile")
    public void compileHandler(ClickEvent e) {
        Parser p = new Parser();
        Exe exe = new Exe();
        ParsingContext ctx = new ParsingContext(sourceCode.getText());
        exeWindow.clear();
        try {
            while(ctx.hasNext()){
                ParseToken token = p.parseLine(ctx.nextLine());
                exe.insert(token);
            }
            errorWindow.setText("Finished parsing");
            while(exe.hasNext()) {
                this.exeWindow.addItem(exe.next());
            }
        } catch(Exception ex) {
            errorWindow.setText(ex.getMessage());
        }
    }
}
