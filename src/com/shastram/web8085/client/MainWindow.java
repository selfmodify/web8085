package com.shastram.web8085.client;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
    VerticalPanel memoryWindowAddress;

    @UiField
    Button stepButton;

    @UiField
    VerticalPanel registerWindowValues;
    @UiField
    VerticalPanel registerWindowNames;

    @UiField
    Button memoryScrollUp;

    @UiField
    Button memoryScrollDown;
    private final Exe exe = new Exe();
    private final NumberFormat formatter = NumberFormat.getFormat("0000");

    private HashMap<String, TextBox> registerValueMap;
    private int memoryStart = 0;

    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        Style style = new Style(); // create a dummy one
        Style.style = style;
        sourceCode.setHTML("mvi b,2</br>mov a,b</br>mov c,b");
        createMemoryWindowItems();
        createRegisterWindowNames();
        refreshRegisters();
        fillMemoryWindow(false);
        // refresh the register and memory window to remove the red highlight
        refreshRegisters();
    }

    private void createRegisterWindowNames() {
        registerWindowNames.clear();
        String[] names = { "a", "b", "c", "d", "e", "h", "l", "sp", "psw", "ip" };
        registerValueMap = new HashMap<String, TextBox>();
        for (String n : names) {
            TextBox name = createValueTextbox();
            name.setText(n);
            name.setReadOnly(true);
            registerWindowNames.add(name);
            TextBox value = createValueTextbox();
            registerValueMap.put(n, value);
            registerWindowValues.add(value);
        }
    }

    private void createMemoryWindowItems() {
        memoryWindow.clear();
        for (int i = 0; i < 10; ++i) {
            TextBox addr = createValueTextbox();
            addr.setReadOnly(true);
            memoryWindowAddress.add(addr);
            TextBox value = createValueTextbox();
            memoryWindow.add(value);
        }
    }

    public TextBox createValueTextbox() {
        TextBox addr = new TextBox();
        addr.addStyleName(Style.style.css.memoryTextBox());
        return addr;
    }

    private void fillMemoryWindow(boolean highlight) {
        int start = memoryStart;
        for (int i = 0; i < memoryWindow.getWidgetCount(); ++i) {
            int addr = start + i;
            TextBox addrBox = (TextBox) memoryWindowAddress.getWidget(i);
            addrBox.setText(" " + formatter.format(addr) + ":  ");
            TextBox value = (TextBox) memoryWindow.getWidget(i);
            updateTextboxValue(exe.getMemory(addr), value, highlight);
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
            fillMemoryWindow(false);
            // refresh the register and memory window to remove the red highlight
            refreshRegisters();
        } catch (Exception ex) {
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
            fillMemoryWindow(true);
        }
    }

    public void refreshRegisters() {
        updateTextboxValue(exe.a, registerValueMap.get("a"));
        updateTextboxValue(exe.b, registerValueMap.get("b"));
        updateTextboxValue(exe.c, registerValueMap.get("c"));
        updateTextboxValue(exe.d, registerValueMap.get("d"));
        updateTextboxValue(exe.e, registerValueMap.get("e"));
        updateTextboxValue(exe.h, registerValueMap.get("h"));
        updateTextboxValue(exe.l, registerValueMap.get("l"));
        updateTextboxValue(exe.l, registerValueMap.get("sp"));
        updateTextboxValue(exe.l, registerValueMap.get("psw"));
        updateTextboxValue(exe.ip, registerValueMap.get("ip"));
    }

    private void updateTextboxValue(int v, TextBox textBox, boolean highlight) {
        String oldValue = textBox.getText().trim();
        String newValue = toHex(v);
        textBox.setText(newValue);
        if (!oldValue.equals(newValue) && highlight) {
            textBox.addStyleName(Style.style.css.memoryWindowHighlight());
        } else {
            textBox.removeStyleName(Style.style.css.memoryWindowHighlight());
        }
    }

    private void updateTextboxValue(int v, TextBox textBox) {
        updateTextboxValue(v, textBox, true);
    }

    private String toHex(int i) {
        String str = Integer.toHexString(i);
        if (str.length() == 1) {
            str = "0" + str;
        }
        str = str.toUpperCase();
        return str;
    }

    @UiHandler("memoryScrollUp")
    void memoryScrollUpHandler(ClickEvent e) {
        if (memoryStart < 65536) {
            ++memoryStart;
            fillMemoryWindow(false);
        }
    }

    @UiHandler("memoryScrollDown")
    void memoryScrollDownHandler(ClickEvent e) {
        if (memoryStart > 0) {
            --memoryStart;
            fillMemoryWindow(false);
        }
    }
}
