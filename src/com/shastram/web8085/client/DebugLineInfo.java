package com.shastram.web8085.client;

public class DebugLineInfo {

    int line;
    int startIndex;
    int endIndex;
    private final String token;

    public DebugLineInfo(String token, int line, int start, int end) {
        this.token = token;
        this.line = line;
        this.startIndex = start;
        this.endIndex = end;
    }

    public String getToken() {
        return token;
    }
}
