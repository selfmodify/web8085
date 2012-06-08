package com.shastram.web8085.client;

public class InstructionParser {
    public enum Mnemonic {
        ACI,
        ADC,
        ADI,
        CMC,
        DAD,
        DCR,
        DCX,
        INR,
        INX,
        NONE,
        MOV,
        ADD,
        SUB,
        SUI,
        SBB,
        SBI,
        ASSERT,
        BREAK,
        MVI,
        LDA,
        LDAX,
        LHLD,
        SHLD,
        STA,
        STAX,
        LXI,
        XCHG,
        STC,
    }

    public enum Operand {
        B, C, D, E, H, L, M, A, PSW, SP
    }

    public Operand op1 = null;
    public Operand op2 = null;
    public Mnemonic mnemonic = Mnemonic.NONE;
    public int baseCode = -1;
    public int code = -1;
    public String name;
    private final OperandParser oparser;
    public int ip;
    private int immediate;
    private boolean hasImmediate;
    int len = 1;

    public InstructionParser(Mnemonic type, int code, OperandParser oparser) {
        this.mnemonic = type;
        this.name = type.name().toLowerCase();
        this.baseCode = code;
        this.oparser = oparser;
    }

    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    public void parseOperands(Parser parser, String operands, int ip) throws Exception {
        this.ip = ip;
        oparser.parse(parser, this, operands);
    }

    public void setImmediate16Bit(int num) {
        hasImmediate = true;
        immediate = num & 0xffff;
        len = 3;
    }

    public int getImmediate() throws ParserException {
        if (!hasImmediate) {
            throw new ParserException("Instruction does not have an immediate operand.");
        }
        return immediate;
    }

    public boolean hasImmediate() {
        return hasImmediate;
    }

    public void setImmediate8Bit(int num) {
        hasImmediate = true;
        immediate = num & 0xff;
        len = 2;
    }
}
