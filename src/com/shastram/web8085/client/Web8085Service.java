package com.shastram.web8085.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.shastram.web8085.client.rpc.SaveFileData;

/**
 * RPC Service
 * 
 * @author vijay
 * 
 */
@RemoteServiceRelativePath("rpc")
public interface Web8085Service extends RemoteService {

    /**
     * Get the names of all the examples
     * 
     * @return
     */
    List<String> getExampleNames();

    /**
     * Get the source code of one example
     * 
     * @param name
     * @return
     */
    String getExampleSourceCode(String name);

    String saveFile(SaveFileData saveFileData);

    String getTicket() throws Exception;
}
