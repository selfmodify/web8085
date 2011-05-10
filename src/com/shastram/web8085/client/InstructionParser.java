package com.shastram.web8085.client;


public class InstructionParser {
    public enum Mnemonic {
        ACI,
        NONE,
        MOV,
        ADD,
        SUB,
        ASSERT,
        MVI,
        LDA,
    }

    public enum Operand {
        B,C,D,E,H,L,M,A,PSW,SP
    }

    public Operand op1 = null;
    public Operand op2 = null;
    public Mnemonic mnemonic = Mnemonic.NONE;
    public int baseCode = -1;
    public int code = -1;
    public String name;
    private OperandParser oparser;
    public int ip;
    private short immediate;
    private boolean hasImmediate;

    public InstructionParser(Mnemonic type, int code, OperandParser oparser) {
        this.mnemonic = type;
        this.name = type.name().toLowerCase();
        this.baseCode = code;
        this.oparser = oparser;
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void parseOperands(Parser parser, String operands) throws Exception {
        oparser.parse(parser, this, operands);
    }

    public void setImmediate(short num) {
        hasImmediate = true;
        immediate = num;
    }

    public short getImmediate( ) throws ParserException {
        if(!hasImmediate) {
            throw new ParserException("Instruction does not have an immediate operand.");
        }
        return immediate;
    }

    public boolean hasImmediate() {
        return hasImmediate;
    }
}
