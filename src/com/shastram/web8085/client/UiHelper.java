package com.shastram.web8085.client;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Utility functions to help the UI
 * @author vijay
 *
 */
public class UiHelper {

    private static final String BOX_NET_TICKET = "box.net.ticket";
    private static final String BOX_NET_AUTH = "box.net.auth";
    private static final String StorageKey = "sourceCode";
    private static Logger logger = Logger.getLogger(UiHelper.class.getName());

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

    /**
     * Save the auth_token and ticket from the URL into cookies.
     */
    public static boolean saveTicketAndAuthcodeFromUrl() {
        boolean authCodeSaved = false;
        Map<String, List<String>> map = Window.Location.getParameterMap();
        List<String> authCodeList = map.get("auth_token");
        List<String> ticketList = map.get("ticket");
        if (authCodeList!=null && !authCodeList.isEmpty()) {
            Cookies.setCookie(BOX_NET_AUTH, authCodeList.get(0));
            authCodeSaved = true;
        }
        if (ticketList != null && !ticketList.isEmpty()) {
            Cookies.setCookie(BOX_NET_TICKET, ticketList.get(0));
        }
        return authCodeSaved;
    }
    
    public static String getAuthToken() {
        return Cookies.getCookie(BOX_NET_AUTH);
    }

    public static void clearBoxNetAuthCookies() {
        Cookies.removeCookie(BOX_NET_AUTH);
        Cookies.removeCookie(BOX_NET_TICKET);
    }
}
