package com.shastram.web8085.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPC Service
 * 
 */
@RemoteServiceRelativePath("rpc")
public interface Web8085Service extends RemoteService {

    /**
     * Get the names of all the examples
     */
    List<String> getExampleNames();

    String saveToDrive(String src);

    public LoginData getLoginData(LoginData d);

    ServiceResponse saveFile(FileData data);

    ServiceResponse listFiles();
    ServiceResponse openFile(FileInfo fileInfo);
}
