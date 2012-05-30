package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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
    VerticalPanel flagNames;

    @UiField
    VerticalPanel flagValues;

    @UiField
    Button memoryScrollUp;

    @UiField
    Button memoryScrollDown;

    @UiField
    CheckBox memoryFollowIp;
    private static Logger logger = Logger.getLogger(MainWindow.class.getName());
    private final Exe exe = new Exe();
    private final NumberFormat formatter = NumberFormat.getFormat("0000");

    private HashMap<String, TextBox> registerValueMap;
    private int memoryStart = 0;

    private HashMap<String, TextBox> flagValueMap;

    private TextBox prevHighlightAddress;

    private TextBox prevHighlightValue;

    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        Style style = new Style(); // create a dummy one
        Style.style = style;
        sourceCode.setHTML("mvi b,2</br>mov a,b</br>mov c,b");
        createMemoryWindowItems();
        createRegisterWindowNames();
        createFlagWindow();
        refreshRegistersAndFlags();
        fillMemoryWindow(false);
        // refresh the register and memory window to remove the red highlight
        refreshRegistersAndFlags();

        // mouse UP/DOWN handler
        memoryScrollDown.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                if (memoryStart > 0) {
                    --memoryStart;
                    fillMemoryWindow(false, false);
                }
            }
        });

        memoryScrollUp.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                if (memoryStart < 65536) {
                    ++memoryStart;
                    fillMemoryWindow(false, false);
                }
            }
        });
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

    private void createFlagWindow() {
        flagNames.clear();
        flagValues.clear();
        String[] names = { "Sign", "Zero", "AuxCy", "Parity", "Carry" };
        flagValueMap = new HashMap<String, TextBox>();
        for (String n : names) {
            TextBox name = createValueTextbox();
            name.setText(n);
            name.setReadOnly(true);
            flagNames.add(name);
            TextBox value = createValueTextbox();
            flagValueMap.put(n, value);
            flagValues.add(value);
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

    private void fillMemoryWindow(boolean highlight, boolean followIp) {
        if (followIp) {
            boolean changeMemoryStart =
                    memoryStart >= exe.ip &&
                            memoryStart <= memoryStart + memoryWindow.getWidgetCount();
            if (changeMemoryStart) {
                memoryStart = exe.ip;
            }
        }
        removeFollowMemoryHighlight();
        int start = memoryStart;
        for (int i = 0; i < memoryWindow.getWidgetCount(); ++i) {
            int addr = start + i;
            TextBox addrBox = (TextBox) memoryWindowAddress.getWidget(i);
            addrBox.setText(" " + formatter.format(addr) + ":  ");
            TextBox value = (TextBox) memoryWindow.getWidget(i);
            updateTextboxValue(exe.getMemory(addr), value, highlight);
            if (addr == exe.ip) {
                updateFollowMemoryHighlight(addrBox, value);
            }
        }
    }

    private void fillMemoryWindow(boolean highlight) {
        fillMemoryWindow(highlight, memoryFollowIp.isEnabled());
    }

    private void updateFollowMemoryHighlight(TextBox addrBox, TextBox value) {
        removeFollowMemoryHighlight();
        prevHighlightAddress = addrBox;
        prevHighlightValue = value;
        value.addStyleName(Style.style.css.currentInstructionHighlight());
        addrBox.addStyleName(Style.style.css.currentInstructionHighlight());
    }

    public void removeFollowMemoryHighlight() {
        if (prevHighlightAddress != null) {
            prevHighlightAddress.removeStyleName(Style.style.css.currentInstructionHighlight());
        }
        if (prevHighlightValue != null) {
            prevHighlightValue.removeStyleName(Style.style.css.currentInstructionHighlight());
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
            refreshRegistersAndFlags();
            fillMemoryWindow(false);
            // refresh the register and memory window to remove the red highlight
            refreshRegistersAndFlags();
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
            refreshRegistersAndFlags();
            fillMemoryWindow(true);
            highlightCurrentInstruction();
        }
    }

    private void highlightCurrentInstruction() {
        DebugLineInfo info = exe.getSourceLineNumber(exe.ip);
        if (info != null) {
        }
    }

    public void refreshRegistersAndFlags() {
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

        // refresh flags
        updateTextboxValue(exe.sign, flagValueMap.get("Sign"));
        updateTextboxValue(exe.zero, flagValueMap.get("Zero"));
        updateTextboxValue(exe.auxCarry, flagValueMap.get("AuxCy"));
        updateTextboxValue(exe.parity, flagValueMap.get("Parity"));
        updateTextboxValue(exe.carry, flagValueMap.get("Carry"));
    }

    private void updateTextboxValue(boolean sign, TextBox textBox) {
        updateTextboxValue(sign, textBox, true);
    }

    private void updateTextboxValue(boolean v, TextBox textBox, boolean highlight) {
        String oldValue = textBox.getText().trim();
        String newValue = toHex(v);
        textBox.setText(newValue);
        if (!oldValue.equals(newValue) && highlight) {
            textBox.addStyleName(Style.style.css.memoryWindowHighlight());
        } else {
            textBox.removeStyleName(Style.style.css.memoryWindowHighlight());
        }
    }

    private String toHex(boolean v) {
        String str = Integer.toHexString(v ? 1 : 0);
        return str;
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
}
