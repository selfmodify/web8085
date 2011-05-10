package com.shastram.web8085.client;

import java.util.HashMap;

public class Exe {
    public int ip = 0;
    public byte memory[] = new byte[64 * 1024];
    private int counter = 0;

    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int h;
    public int l;

    public boolean sign;
    public boolean zero;
    public boolean auxCarry;
    public boolean parity;
    public boolean carry;

    /**
     * map containing the ip address to the assert instruction.
     */
    public HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();

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

    public void insert(int opcode, short immediate) {
        memory[ip++] = (byte)opcode;
        memory[ip++] = (byte)(immediate & 0xff);
        memory[ip++] = (byte)(immediate >> 8);
    }

    public void insert(ParseToken token) throws ParserException {
        switch(token.getType()) {
        case INSTRUCTION:
                InstructionParser i = token.getIx();
                if(i.hasImmediate()) {
                    insert(i.code, i.getImmediate());
                } else {
                    insert(i.code);
                }
                break;
        case ASSERT:

        }
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


    public void setMemory(short addr, short value) {
        memory[addr] = (byte)(value & 0xff);
        memory[addr+1] = (byte)((value>>8) & 0xff);
    }

    public void nextIp2(int len) {
        incrIp(len);
    }

    public void nextIp() {
        incrIp(1);
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
        clearFlags();
    }

    public void resetRegisters() {
        ip = 0;
        a = b = c = d = e = h = l = 0;
        counter = 0;
    }

    public void clearFlags() {
        sign = false;
        zero = false;
        carry = false;
    }

    private void incrIp(int i) {
        ip += i;
        ip = ip % 65536;
    }

    public void setZero() {
        zero = true;
    }

    public void resetZero() {
        zero = false;
    }

    public boolean isZero() {
        return zero;
    }

    public void setSign() {
        sign = true;
    }

    public void resetSign() {
        sign = false;
    }

    public boolean isSign() {
        return sign;
    }

    public void setCarry() {
        carry = true;
    }

    public void resetCarry() {
        carry = false;
    }

    public boolean isCarry() {
        return carry;
    }

    public void setZSFlags() {
        // set the zero bit
        if(a == 0) {
            setZero();
        } else {
            resetZero();
        }

        // set the sign bit
        if( (a & 0x80) == 0x80) {
            setSign();
        } else {
            resetSign();
        }
    }

    public byte getMemAtIp() {
        return memory[ip];
    }

    /**
     * @return 1 if the carry is set else return 0
     */
    public short getCarry() {
        return (short) (carry ? 1 :0);
    }

    /**
     * @return 1 if the sign is set else return 0
     */
    public short getSign() {
        return (short) (sign ? 1 :0);
    }
    /**
     * @return 1 if the zero is set else return 0
     */
    public short getZero() {
        return (short) (zero ? 1 :0);
    }
    /**
     * @return 1 if the parity is set else return 0
     */
    public short getParity() {
        return (short) (parity ? 1 :0);
    }
    /**
     * @return 1 if the aux carry is set else return 0
     */
    public short getAuxCarry() {
        return (short) (auxCarry ? 1 :0);
    }

    public void compileCode(String text) throws Exception {
        Parser p = new Parser(text);
        reset();
        while(p.hasNext()){
            ParseToken token = p.parseNextLine();
            insert(token);
        }
        // copy the assertion map from the parser
        assertOperation = p.getAssertionMap();
        reset();
    }

    public String getAsertionAt(int ip) {
        return assertOperation.get(ip);
    }

    public void showDialog(String string) {
        // TODO Auto-generated method stub

    }

    public void assertionFailed(String string) {
    }

    /**
     * @return Read the 16 bit immediate from ip+1, ip+2.
     * Also increment the ip by 2.
     */
    public int read16bit() {
        int value = memory[ip++];
        value = (memory[ip++]<<8) + value;
        return value;
    }
}
