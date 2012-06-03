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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainWindow extends Composite {

    private static final int NUM_MEMORY_ADDRESS_PER_ROW = 8;

	private static MainWindowUiBinder uiBinder = GWT.create(MainWindowUiBinder.class);

    interface MainWindowUiBinder extends UiBinder<Widget, MainWindow> {
    }

    @UiField
    Button compile;

    @UiField
    TextArea sourceCode;

    @UiField
    TextArea errorWindow;

    @UiField
    VerticalPanel memoryWindow;

    @UiField
    VerticalPanel memoryWindowAddress;

    @UiField
    Button stepButton;

    @UiField
    HorizontalPanel registerWindowValues;

    @UiField
    HorizontalPanel registerWindowValues2;
    @UiField
    HorizontalPanel flagValues;

    @UiField
    Button memoryScrollUp;

    @UiField
    Button memoryScrollDown;

    @UiField
    CheckBox memoryFollowIp;

    @UiField
    Button loadArithmeticButton;

    @UiField
    VerticalPanel disassemblyWindow;

    private static Logger logger = Logger.getLogger(MainWindow.class.getName());
    private final Exe exe = new Exe();
    private final NumberFormat formatter = NumberFormat.getFormat("0000");

    private HashMap<String, HorizontalPanel> registerValueMap;
    private int memoryStart = 0;
    private int disassemblyMemoryStart = 0;

    private HashMap<String, HorizontalPanel> flagValueMap;

    private TextBox prevHighlightAddress;

    private TextBox prevHighlightValue;

    private TextBox prevHighlightDisassemblyAddress;

    private TextBox prevHighlightDisassemblyValue;

    private final int maxMemoryWindowRows = 5;

    private final int maxDisassemblyRows = 8;

    public MainWindow() {
        initWidget(uiBinder.createAndBindUi(this));
        Style style = new Style(); // create a dummy one
        Style.style = style;
        sourceCode.setText("mvi b,2\nmov a,b\nmov c,b");
        createMemoryWindowItems(maxMemoryWindowRows);
        createDisassemblyWindowItems(maxDisassemblyRows);
        createRegisterWindowNames();
        createFlagWindow();
        refreshRegistersAndFlags();
        fillMemoryWindow(false);
        fillDisassemblyWindow();
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

    private void createDisassemblyWindowItems(int maxRows) {
        disassemblyWindow.clear();
        for (int i = 0; i < maxRows; ++i) {
            HorizontalPanel hp = new HorizontalPanel();
            TextBox addr = createMemoryAddressTextbox();
            hp.add(addr);
            TextBox value = createValueTextbox(Style.style.css.disassemblyTextBox());
            hp.add(value);
            disassemblyWindow.add(hp);
        }
    }

    private void fillDisassemblyWindow() {
        boolean followIp = true;
        if (followIp) {
            boolean changeMemoryStart =
                    exe.ip < disassemblyMemoryStart ||
                            exe.ip >= disassemblyMemoryStart + disassemblyWindow.getWidgetCount();
            if (changeMemoryStart) {
            	disassemblyMemoryStart = exe.ip;
            }
        }
        removeFollowMemoryHighlight(prevHighlightDisassemblyAddress, prevHighlightDisassemblyValue);
        int addr = disassemblyMemoryStart;
        for (int i = 0; i < disassemblyWindow.getWidgetCount(); ++i) {
            HorizontalPanel hp = (HorizontalPanel) disassemblyWindow.getWidget(i);
            TextBox addrBox = (TextBox) hp.getWidget(0);
            addrBox.setText(" " + formatter.format(addr) + ":  ");
            TextBox valueBox = (TextBox) hp.getWidget(1);
            DebugLineInfo debugInfo = exe.getDebugInfo(addr);
            if (addr == exe.ip) {
                updateFollowMemoryHighlight(addrBox, valueBox);
                prevHighlightDisassemblyAddress = addrBox;
                prevHighlightDisassemblyValue = valueBox;
            }
            if (debugInfo != null) {
                valueBox.setText(debugInfo.getToken());
                addr += debugInfo.getInstructionLength();
            } else {
                valueBox.setText("nop");
                ++addr;
            }
        }
    }

    private void createRegisterWindowNames() {
        registerWindowValues.clear();
        registerWindowValues2.clear();
        registerValueMap = new HashMap<String, HorizontalPanel>();
        String[] names = { "a", "b", "c", "d", "e" };
        String[] names2 = { "h", "l", "sp", "psw", "ip" };
        addLabelValuePairToPanel(names, registerWindowValues);
        addLabelValuePairToPanel(names2, registerWindowValues2);
    }

    public void addLabelValuePairToPanel(String[] names, HorizontalPanel parentPanel) {
        for (String n : names) {
            TextBox name = createRegisterValueTextbox();
            name.setText(n);
            name.setReadOnly(true);
            TextBox value = createRegisterValueTextbox();
            HorizontalPanel hp = createRegOrFlagBox(name, value);
            registerValueMap.put(n, hp);
            parentPanel.add(hp);
        }
    }

    public HorizontalPanel createRegOrFlagBox(TextBox name, TextBox value) {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(0);
        hp.add(name);
        hp.add(value);
        Label l = new Label("|");
        l.setWidth("9px");
        hp.add(l);
        return hp;
    }

    private void createFlagWindow() {
        flagValues.clear();
        String[] names = { "S", "Z", "Aux", "P", "Cy" };
        flagValueMap = new HashMap<String, HorizontalPanel>();
        for (String n : names) {
            TextBox name = createRegisterValueTextbox();
            name.setText(n);
            name.setReadOnly(true);
            TextBox value = createRegisterValueTextbox();
            HorizontalPanel hp = createRegOrFlagBox(name, value);
            flagValueMap.put(n, hp);
            flagValues.add(hp);
        }
    }

    private void createMemoryWindowItems(int maxRows) {
        memoryWindow.clear();
        for (int i = 0; i < maxRows; ++i) {
            HorizontalPanel hp = new HorizontalPanel();
            TextBox addr = createMemoryAddressTextbox();
            memoryWindowAddress.add(addr);
            for (int j = 0; j < NUM_MEMORY_ADDRESS_PER_ROW; ++j) {
                TextBox value = createValueTextbox(Style.style.css.memoryTextBox());
                hp.add(value);
            }
            memoryWindow.add(hp);
        }
    }

    public TextBox createValueTextbox(String style) {
        TextBox addr = new TextBox();
        addr.addStyleName(style);
        return addr;
    }

    public TextBox createMemoryAddressTextbox() {
        TextBox addr = new TextBox();
        addr.addStyleName(Style.style.css.memoryAddressTextBox());
        addr.setReadOnly(true);
        return addr;
    }

    public TextBox createRegisterValueTextbox() {
        TextBox addr = new TextBox();
        addr.addStyleName(Style.style.css.registerTextBox());
        return addr;
    }

    private void fillMemoryWindow(boolean highlight, boolean followIp) {
        if (followIp) {
            boolean changeMemoryStart =
                    exe.ip < memoryStart ||
                            exe.ip >= memoryStart + memoryWindow.getWidgetCount() * NUM_MEMORY_ADDRESS_PER_ROW;
            if (changeMemoryStart) {
                memoryStart = exe.ip;
            }
        }
        removeFollowMemoryHighlight(prevHighlightAddress, prevHighlightValue);
        int start = memoryStart;
        int addr = start;
        for (int i = 0; i < memoryWindow.getWidgetCount(); ++i) {
            TextBox addrBox = (TextBox) memoryWindowAddress.getWidget(i);
            addrBox.setText(" " + formatter.format(addr) + ":  ");
            HorizontalPanel hp = (HorizontalPanel) memoryWindow.getWidget(i);
            for (int j = 0; j < hp.getWidgetCount(); ++j) {
                TextBox valueTextbox = (TextBox) hp.getWidget(j);
                String newValue = toHex(exe.getMemory(addr));
                updateTextboxValue(newValue, valueTextbox, highlight);
                if (addr == exe.ip) {
                    updateFollowMemoryHighlight(addrBox, valueTextbox);
                    prevHighlightAddress = addrBox;
                    prevHighlightValue = valueTextbox;
                }
                ++addr;
            }
        }
    }

    private void fillMemoryWindow(boolean highlight) {
        fillMemoryWindow(highlight, memoryFollowIp.getValue());
    }

    private void updateFollowMemoryHighlight(TextBox addrBox, TextBox value) {
        value.addStyleName(Style.style.css.currentInstructionHighlight());
        addrBox.addStyleName(Style.style.css.currentInstructionHighlight());
    }

    public void removeFollowMemoryHighlight(TextBox prevAddr, TextBox prevValue) {
        if (prevAddr != null) {
            prevAddr.removeStyleName(Style.style.css.currentInstructionHighlight());
        }
        if (prevValue != null) {
            prevValue.removeStyleName(Style.style.css.currentInstructionHighlight());
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
            fillDisassemblyWindow();
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
            if (exe.hltExecuted) {
                errorWindow.setText("HLT Executed at ip=" + exe.ip);
            } else {
                DebugLineInfo debugInfo = exe.getDebugInfo(exe.ip);
                if (debugInfo != null) {
                    errorWindow.setText("Next instruction, Line: " + debugInfo.line + " " + debugInfo.getToken());
                }
            }
        } catch (Exception e1) {
            errorWindow.setText(e1.getMessage());
        } finally {
            refreshRegistersAndFlags();
            fillMemoryWindow(true);
            fillDisassemblyWindow();
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
        updateTextboxValue(exe.sign, flagValueMap.get("S"));
        updateTextboxValue(exe.zero, flagValueMap.get("Z"));
        updateTextboxValue(exe.auxCarry, flagValueMap.get("Aux"));
        updateTextboxValue(exe.parity, flagValueMap.get("P"));
        updateTextboxValue(exe.carry, flagValueMap.get("Cy"));
    }

    private void updateTextboxValue(boolean sign, HorizontalPanel hp) {
        if (hp.getWidgetCount() > 1) {
            TextBox textBox = (TextBox) hp.getWidget(1);
            String newValue = toHex(sign);
            updateTextboxValue(newValue, textBox, true);
        }
    }

    private String toHex(boolean v) {
        String str = Integer.toHexString(v ? 1 : 0);
        return str;
    }

    private void updateTextboxValue(String newValue, TextBox textBox, boolean highlight) {
        String oldValue = textBox.getText().trim();
        textBox.setText(newValue);
        if (!oldValue.equals(newValue) && highlight) {
            textBox.addStyleName(Style.style.css.memoryWindowHighlight());
        } else {
            textBox.removeStyleName(Style.style.css.memoryWindowHighlight());
        }
    }

    private void updateTextboxValue(int v, HorizontalPanel hp) {
        if (hp.getWidgetCount() > 1) {
            TextBox textBox = (TextBox) hp.getWidget(1);
            updateTextboxValue(v, textBox);
        }
    }

    private void updateTextboxValue(int v, TextBox textBox) {
        String newValue = toHex(v);
        updateTextboxValue(newValue, textBox, true);
    }

    private String toHex(int i) {
        String str = Integer.toHexString(i);
        if (str.length() == 1) {
            str = "0" + str;
        }
        str = str.toUpperCase();
        return str;
    }

    @UiHandler("loadArithmeticButton")
    void loadArithmeticButtonHandler(ClickEvent e) {
        sourceCode.setText("# adi - Add Immediate Data to Accumulator\n" +
                " mvi a,26h\n" +
                " stc\n" +
                " aci 57h\n" +
                " .assert a=7eh, s=0, z=0, ac=0, p=1, cy=0\n");
    }
}
