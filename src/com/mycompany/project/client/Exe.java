package com.mycompany.project.client;

public class Exe {
    private int ip = 0;
    private byte memory[] = new byte[64 * 1024];
    private int counter = 0;
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
}
