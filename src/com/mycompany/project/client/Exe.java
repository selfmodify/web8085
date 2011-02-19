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
        int ix = (int)memory[counter];
        if(ix < 0) {
            ix = 256 + ix;
        }
        counter++;
        String str = Instruction.toString(ix, 0);
        return str;
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
        int opcode = (int)memory[counter];
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
