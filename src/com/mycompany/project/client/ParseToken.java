package com.mycompany.project.client;

public class ParseToken {
    private Instruction ix;
    private Type type;
    private String token;

    public ParseToken(Instruction instruction, String token) {
        this.ix = instruction;
        this.token = token;
        type = Type.INSTRUCTION;
    }

    public ParseToken(Type type, String token) {
        this.type = type;
        this.token = token;
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

    public Instruction getIx() {
        return ix;
    }

    public void setIx(Instruction ix) {
        this.ix = ix;
    }
}
