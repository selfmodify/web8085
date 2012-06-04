package com.shastram.web8085.client;

public class ParserException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 478421858191213908L;

	public ParserException(String string) {
        super(string);
    }

	public ParserException(String msg, Exception e) {
		super(msg, e);
	}

}
