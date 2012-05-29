package com.shastram.web8085.client;

public class DebugLineInfo {

    int line;
    int startIndex;
    int endIndex;

    public DebugLineInfo(int line, int start, int end) {
        this.line = line;
        this.startIndex = start;
        this.endIndex = end;
    }
}
