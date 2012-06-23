package com.shastram.web8085.client;

import com.shastram.web8085.client.Parser.PerInstructionToken;

public class ParseToken {
    private Parser.PerInstructionToken ix;
    private TokenType type;
    private String token;
    private int lineNumber;
    private int startColumn;
    private int endColumn;
    private String firstToken;
    private int ip;
    private String[] tokenParts;

    public ParseToken(Parser.PerInstructionToken instruction,
            String firstToken, String lineStr, int lineNumber, int startColumn,
            int endColumn) {
        this.ix = instruction;
        this.firstToken = firstToken;
        this.token = lineStr;
        this.setLineNumber(lineNumber);
        this.setStartColumn(startColumn);
        this.endColumn = endColumn;
        // TODO: Maybe there is a better way to differentiate between asserts
        // and instructions?
        type = instruction.name.equalsIgnoreCase("assert") ? TokenType.ASSERT
                : TokenType.INSTRUCTION;
    }

    public ParseToken(TokenType type, String token) {
        this.type = type;
        this.token = token;
    }

    public ParseToken(int lineNumber, TokenType type, int ip,
            String firstToken, String[] allTokens) {
        this.lineNumber = lineNumber;
        this.firstToken = firstToken;
        this.ip = ip;
        this.type = type;
        this.tokenParts = allTokens;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
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

    public String[] getTokenParts() {
        return tokenParts;
    }
}
