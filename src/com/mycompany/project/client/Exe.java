package com.mycompany.project.client;

public class Exe {
    public int ip = 0;
    private byte memory[] = new byte[64 * 1024];
    private int counter = 0;

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int h;
    public int l;

    public void insert(int opcode, int op1, int op2) {
        memory[ip++] = (byte)opcode;
        memory[ip++] = (byte)op1;
        memory[ip++] = (byte)op2;
    }

    public void insert(int opcode, int op1) {
        memory[ip++] = (byte)opcode;
        memory[ip++] = (byte)op1;
    }

    public void insert(int opcode) {
        memory[ip++] = (byte)opcode;
    }

    public void insert(ParseToken token) {
        InstructionParser i = token.getIx();
        insert(i.code);
    }

    public boolean hasNext() {
        return counter < ip;
    }

    public String next() {
        int ix = memory[counter];
        if(ix < 0) {
            ix = 256 + ix;
        }
        counter++;
        String str = Instruction.toString(ix, 0);
        return str;
    }

    public short getRegOrMem(int i) {
        int v =
            getRegOrMemInternal(i);
        return (short)v;
    }

    private int getRegOrMemInternal(int i) {
        switch(i) {
        case 0: return b;
        case 1: return c;
        case 2: return d;
        case 3: return e;
        case 4: return h;
        case 5: return l;
        case 6: return memory[ip];
        case 7: return a;
        default:
            throw new IllegalStateException("Invalid register index in get" + i);
        }
    }

    public void setRegOrMem(int i, int intValue) {
        byte value = (byte)intValue;
        switch(i) {
        case 0: b = value; break;
        case 1: c = value; break;
        case 2: d = value; break;
        case 3: e = value; break;
        case 4: h = value; break;
        case 5: l = value; break;
        case 6: memory[ip] = value; break;
        case 7: a = value; break;
        default:
            throw new IllegalStateException("Invalid register index in set" + i);
        }
    }


    public void nextIp2(int len) {
        ip += len;
    }

    public void nextIp() {
        ++ip;
    }

    public void step() throws Exception {
        Instruction.execute(this);
    }

    public int getOpcode() {
        int opcode = memory[counter];
        if(opcode < 0) {
            opcode = 256 + opcode;
        }
        return opcode;
    }

    public int getIp() {
        return ip;
    }

    public void reset() {
        resetRegisters();
    }

    public void resetRegisters() {
        ip = 0;
        a = b = c = d = e = h = l = 0;
        counter = 0;
    }
}
