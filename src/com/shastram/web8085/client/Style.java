package com.shastram.web8085.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Style extends Composite {

    private static StyleUiBinder uiBinder = GWT.create(StyleUiBinder.class);

    public interface Css extends CssResource {
        String memoryTextBox();

        String memoryAddressTextBox();

        String memoryWindowHighlight();

        String currentInstructionHighlight();

        String currentAddressHighlight();

        String registerTextBox();

        String disassemblyTextBox();

        String exampleMenuItems();
    }

    @UiField
    Css css;

    // public static reference
    public static Style style;

    interface StyleUiBinder extends UiBinder<Widget, Style> {
    }

    public Style() {
        initWidget(uiBinder.createAndBindUi(this));
        Style.style = this;
    }

}
