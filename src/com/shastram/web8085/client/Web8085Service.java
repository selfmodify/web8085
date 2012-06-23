package com.shastram.web8085.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPC Service
 * @author vijay
 *
 */
@RemoteServiceRelativePath("rpc")
public interface Web8085Service extends RemoteService {
	
	/**
	 * Get the names of all the examples
	 * @return
	 */
	List<String> getExampleNames();
	
	/**
	 * Get the source code of one example
	 * @param name
	 * @return
	 */
	String getExampleSourceCode(String name);
}
