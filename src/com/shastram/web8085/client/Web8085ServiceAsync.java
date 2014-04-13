package com.shastram.web8085.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface Web8085ServiceAsync {

    void getExampleNames(AsyncCallback<List<String>> callback);

    void getExampleSourceCode(String name, AsyncCallback<String> callback);

    void saveToDrive(String src, AsyncCallback<String> callback);

    void getLoginData(LoginData d, AsyncCallback<LoginData> callback);

    void saveFile(FileData data, AsyncCallback<ServiceResponse> callback);

    void listFiles(AsyncCallback<ServiceResponse> callback);
}
