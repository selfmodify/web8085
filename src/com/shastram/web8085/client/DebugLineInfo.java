package com.shastram.web8085.client;

public class DebugLineInfo {

    int line;
    int startIndex;
    int endIndex;
    private final String token;
    private int instructionLength;

    public DebugLineInfo(String token, int line, int start, int end, int len) {
        this.token = token;
        this.line = line;
        this.startIndex = start;
        this.endIndex = end;
        instructionLength = len;
    }

    public String getToken() {
        return token;
    }

    public void setInstructionLength(int len) {
        this.instructionLength = len;
    }

    public int getInstructionLength() {
        return instructionLength;
    }
}
