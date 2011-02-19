package com.mycompany.project.client;


public class InstructionParser {
    public enum Mnemonic {
        NONE,
        MOV,
        ADD,
        SUB,
    }
    
    public enum Operand {
        B,C,D,E,H,L,M,A,PSW,SP
    }
    
    public Operand op1 = null;
    public Operand op2 = null;
    public Mnemonic type = Mnemonic.NONE;
    public int baseCode = -1;
    public int code = -1;
    public String name;
    private OperandParser oparser;
    
    public InstructionParser(Mnemonic type, int code, OperandParser oparser) {
        this.type = type;
        this.name = type.name().toLowerCase();
        this.baseCode = code;
        this.oparser = oparser;
    }

    public Mnemonic getType() {
        return type;
    }
    
    public void parseOperands(String line) throws Exception {
        oparser.parse(this, line);
    }
    
}
