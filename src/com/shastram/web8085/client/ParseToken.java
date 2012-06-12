package com.shastram.web8085.client;

import com.shastram.web8085.client.Parser.PerInstructionToken;

public class ParseToken {
    private Parser.PerInstructionToken ix;
    private Type type;
    private String token;
    private int lineNumber;
    private int startColumn;
    private int endColumn;
    private String firstToken;
    private int ip;

    public ParseToken(Parser.PerInstructionToken instruction,
            String firstToken,
            String lineStr,
            int lineNumber,
            int startColumn,
            int endColumn) {
        this.ix = instruction;
        this.firstToken = firstToken;
        this.token = lineStr;
        this.setLineNumber(lineNumber);
        this.setStartColumn(startColumn);
        this.endColumn = endColumn;
        // TODO: Maybe there is a better way to differentiate between asserts and instructions?
        type = instruction.name.equalsIgnoreCase("assert") ? Type.ASSERT : Type.INSTRUCTION;
    }

    public ParseToken(Type type, String token) {
        this.type = type;
        this.token = token;
    }

    public ParseToken(int lineNumber, Type type, int ip, String firstToken, String[] allTokens) {
        this.lineNumber = lineNumber;
        this.firstToken = firstToken;
        this.ip = ip;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PerInstructionToken getIx() {
        return ix;
    }

    public void setIx(PerInstructionToken ix) {
        this.ix = ix;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int line) {
        this.lineNumber = line;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public String getFirstToken() {
        return firstToken;
    }

    public int getIp() {
        return ip;
    }
}
