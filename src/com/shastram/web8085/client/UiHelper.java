package com.shastram.web8085.client;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Utility functions to help the UI
 * @author vijay
 *
 */
public class UiHelper {

    private static final String StorageKey = "sourceCode";
    
    public static void loadSourceCodeLocally(TextArea sourceCode) {
        Storage storage = Storage.getLocalStorageIfSupported();
        if (storage != null) {
            sourceCode.setText(storage.getItem(StorageKey));
        }
    }

    public static void saveSourceCodeLocally(TextArea sourceCode) {
        Storage storage = Storage.getLocalStorageIfSupported();
        if (storage != null) {
            storage.setItem(StorageKey, sourceCode.getText());
        }
    }
}
