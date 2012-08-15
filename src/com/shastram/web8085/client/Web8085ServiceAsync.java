package com.shastram.web8085.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.shastram.web8085.client.rpc.SaveFileData;

public interface Web8085ServiceAsync {

    void getExampleNames(AsyncCallback<List<String>> callback);

    void getExampleSourceCode(String name, AsyncCallback<String> callback);

    void saveFile(SaveFileData saveFileData, AsyncCallback<String> callback);
}
