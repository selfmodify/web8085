package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.shastram.web8085.client.InstructionParser.Operand;

public class Exe {
    private static Logger logger = Logger.getLogger(Exe.class.getName());

    public int ip = 0;
    public byte memory[] = new byte[64 * 1024];
    private int counter = 0;

    private int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int h;
    public int l;
    public int sp;

    public boolean sign;
    public boolean zero;
    public boolean auxCarry;
    public boolean parity;
    public boolean carry;

    /**
     * map containing the ip address to the assert instruction.
     */
    public HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();
    private final HashMap<Integer, DebugLineInfo> debugInfo = new HashMap<Integer, DebugLineInfo>();
    public boolean hltExecuted = false;

    private String context;

    public void insert(int opcode, int op1, int op2) {
        //TODO: Change ip++ to use incrIp
        setMemoryByte(ip, (byte) opcode);
        incrIp();
        setMemoryByte(ip, (byte) op1);
        incrIp();
        setMemoryByte(ip, (byte) op2);
        incrIp();
    }

    public void insert(int opcode, int op1) {
        //TODO: Change ip++ to use incrIp
        setMemoryByte(ip, (byte) opcode);
        incrIp();
        setMemoryByte(ip, (byte) op1);
        incrIp();
    }

    public void insert(int opcode) {
        setMemoryByte(ip, (byte) opcode);
        incrIp();
    }

    public void insertCodeAnd16bit(int opcode, int immediate) {
        setMemoryByte(ip, (byte) opcode);
        incrIp();
        setMemoryByte(ip, (byte) (immediate & 0xff));
        incrIp();
        setMemoryByte(ip, (byte) ((immediate & 0xff00) >> 8));
        incrIp();
    }

    public void insert(int opcode, byte immediate) {
        insert(opcode, immediate);
    }

    public void insert(ParseToken token) throws ParserException {
        InstructionParser i = token.getIx();
        addDebugInfo(token);
        switch (token.getType()) {
        case INSTRUCTION:
            if (i.hasImmediate()) {
                if (i.len == 3) {
                    insertCodeAnd16bit(i.code, i.getImmediate());
                } else {
                    insert(i.code, i.getImmediate());
                }
            } else {
                insert(i.code);
            }
            break;
        case ASSERT:
            if (!Config.ignoreAsserts) {
                insert(i.code);
            }
            break;
        case SYNTAX_ERROR: {
            String msg = "Sytax error parsing at line " + token.getLineNumber() + " : " + token.getToken();
            logger.log(Level.SEVERE, msg);
            throw new ParserException(msg);
        }
        }
    }

    private void addDebugInfo(ParseToken token) {
        InstructionParser ix = token.getIx();
        DebugLineInfo info = new DebugLineInfo(token.getToken(),
                token.getLineNumber(),
                0,
                0,
                ix == null ? 0 : ix.len);
        debugInfo.put(ip, info);
    }

    public boolean hasNext() {
        return counter < ip;
    }

    public String next() {
        int ix = getMemory(counter);
        if (ix < 0) {
            ix = 256 + ix;
        }
        String str = Instruction.toString(ix, 0);
        counter++; // this is buggy must be incremented by length of instruction
        return str;
    }

    public int getRegOrMem(int i) {
        int v = getRegOrMemInternal(i) & 0xff;
        return v;
    }

    private int getRegOrMemInternal(int i) {
        switch (i) {
        case 0:
            return b;
        case 1:
            return c;
        case 2:
            return d;
        case 3:
            return e;
        case 4:
            return h;
        case 5:
            return l;
        case 6:
            return getMemAtHL();
        case 7:
            return getA();
        default:
            throw new IllegalStateException("Invalid register index in get" + i);
        }
    }

    public void setRegOrMem(Operand op, int value) {
        setRegOrMem(op.ordinal(), value);
    }

    /**
     * Set the value of a register or memory pointed by hl
     * 
     * @param regOrMem
     *            : 0 = b, 1=c ... 6=[M], 7=A
     * @param intValue
     *            : The byte value to be set
     */
    public void setRegOrMem(int regOrMem, int value) {
        value = value & 0xff;
        switch (regOrMem) {
        case 0:
            b = value;
            break;
        case 1:
            c = value;
            break;
        case 2:
            d = value;
            break;
        case 3:
            e = value;
            break;
        case 4:
            h = value;
            break;
        case 5:
            l = value;
            break;
        case 6: {
            int addr = getHL();
            setMemoryByte(addr, value);
            break;
        }
        case 7:
            setA(value);
            break;
        default:
            throw new IllegalStateException("Invalid register index in set " + regOrMem);
        }
    }

    public void nextIp2(int len) {
        incrIp(len);
    }

    public void nextIp() {
        incrIp(1);
    }

    public void step() throws Exception {
        Instruction.execute(this);
        if (Config.printRegisters) {
            String str = this.getRegisterValues();
            logger.info(str);
            String memStr = "";
            int end = Config.maxMemToPrint;
            if (end > 0) {
                for (int i = 0; i < end; ++i) {
                    memStr += Integer.toHexString(getMemory(i)) + "h ";
                }
                logger.info("Memory= " + memStr);
            }
        }
    }

    public int getOpcode() {
        int opcode = getMemAtIp();
        if (opcode < 0) {
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
        hltExecuted = false;
        ip = 0;
    }

    public void resetRegisters() {
        ip = 0;
        setA(b = c = d = e = h = l = 0);
        sp = 0;
        counter = 0;
    }

    public void clearFlags() {
        sign = false;
        zero = false;
        carry = false;
    }

    private void incrIp() {
        incrIp(1);
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
        if (getA() == 0) {
            setZero();
        } else {
            resetZero();
        }

        // set the sign bit
        if ((getA() & 0x80) == 0x80) {
            setSign();
        } else {
            resetSign();
        }
    }

    public int getMemAtIp() {
        int value = getMemory(ip);
        return value;
    }

    public int getMemAtHL() {
        int value = getMemory(getHL());
        return value;
    }

    /**
     * @return 1 if the carry is set else return 0
     */
    public int getCarry() {
        return carry ? 1 : 0;
    }

    /**
     * @return 1 if the sign is set else return 0
     */
    public int getSign() {
        return (sign ? 1 : 0);
    }

    /**
     * @return 1 if the zero is set else return 0
     */
    public int getZero() {
        return (zero ? 1 : 0);
    }

    /**
     * @return 1 if the parity is set else return 0
     */
    public int getParity() {
        return (parity ? 1 : 0);
    }

    /**
     * @return 1 if the aux carry is set else return 0
     */
    public int getAuxCarry() {
        return (auxCarry ? 1 : 0);
    }

    public void compileCode(String text, String context) throws Exception {
        Parser p = new Parser(text);
        reset();
        this.context = context;
        int startColumn = 0;
        try {
            while (p.hasNext()) {
                String l = p.nextLine();
                startColumn += l.length();
                ParseToken token = p.parseLine(l, ip, startColumn);
                insert(token);
            }
            insert(0x76); // hlt
            // copy the assertion map from the parser
            assertOperation = p.getAssertionMap();
            logger.info("Finished compilation - " + context);
            logger.info("---------------");
            reset();
        } catch (Exception e) {
            throw new ParserException("Parse error: Line "
                    + p.getLineNumber() + " " + e.getMessage()
                    + " Source: " + p.currentLine());
        }
    }

    public String getAsertionAt(int ip) {
        return assertOperation.get(ip);
    }

    public void showDialog(String msg) {
        DebugLineInfo info = getDebugInfo(ip);
        String str = msg + " ip=" + ip
                + info == null ? "" : " (Source line=" + info.line + ") ";
        logger.info(str);
        nextIp();
    }

    public void assertionFailed(String reason) throws Exception {
        DebugLineInfo info = getDebugInfo(ip);
        String str = "Assertion failed at ip=" + ip
                + info == null ? "" : " (Source line=" + info.line + ") "
                + ", " + reason;
        logger.warning(str);
        throw new Exception(str);
    }

    /**
     * @return Read the 16 bit immediate from ip, ip+1. Also increment the ip by
     *         2.
     */
    public int read16bit() {
        int value = getMemAtIp();
        incrIp();
        value = ((getMemAtIp() << 8) & 0xff00) + value;
        incrIp();
        return value;
    }

    public boolean hltExecuted() {
        return hltExecuted;
    }

    public void setB(int value) {
        b = value & 0xff;
    }

    public void setC(int value) {
        c = value & 0xff;
    }

    public void setD(int value) {
        d = value & 0xff;
    }

    public void setE(int value) {
        e = value & 0xff;
    }

    public void setH(int value) {
        h = value & 0xff;
    }

    public void setL(int value) {
        l = value & 0xff;
    }

    public void setA(int value) {
        a = value & 0xff;
    }

    /**
     * Get the register values as a string
     * 
     * @return
     */
    public String getRegisterValues() {
        String str = " ip=" + ip + " a=" + getA() + " b=" + b + " c=" + c + " d=" + d + " e=" + e + " h=" + h + " l="
                + l;
        return str;
    }

    public int getHL() {
        int value = (((h & 0xff) << 8) + (l & 0xff)) & 0xffff;
        return value;
    }

    public void setHL(int value) {
        value = value & 0xffff;
        setH(value >> 8);
        setL(value & 0xff);
    }

    public int getBC() {
        int value = (((b & 0xff) << 8) + (c & 0xff)) & 0xffff;
        return value;
    }

    public void setBC(int value) {
        value = value & 0xffff;
        setB(value >> 8);
        setC(value & 0xff);
    }

    public int getDE() {
        int value = ((d & 0xff) << 8 + (e & 0xff)) & 0xffff;
        return value;
    }

    public void setDE(int value) {
        value = value & 0xffff;
        setD(value >> 8);
        setE(value & 0xff);
    }

    public int getA() {
        return a;
    }

    public int getSP() {
        return sp;
    }

    public void setSP(int value) {
        this.sp = value & 0xffff;
    }

    /**
     * In an arithmetic operation, when a carry is generated by digit D3 and
     * passed to digit D4, the AC flag is set.
     * 
     * @param otherOperand
     */
    public void setAuxCarry(int op) {
        int a1 = a & 0xf;
        int op1 = op & 0xf;
        int partSum = a1 + op1;

        auxCarry = ((partSum & 0x10) != 0);
    }

    public int getRegOrMem(Operand op) {
        switch (op) {
        case B:
            return b;
        case C:
            return c;
        case D:
            return d;
        case E:
            return e;
        case H:
            return h;
        case L:
            return l;
        case A:
            return getA();
        case M: {
            int hl = getHL();
            return getMemory(hl);
        }
        case SP:
            return sp;
        }
        throw new IllegalStateException("Invalid register " + op.toString());
    }

    /**
     * Get 8 bit value at address addr
     * 
     * @param addr
     * @return
     */
    public int getMemory(int addr) {
        addr = normalizeMemoryAddress(addr);
        int value = memory[addr] & 0xff;
        return value;
    }

    private int normalizeMemoryAddress(int addr) {
        return (addr % 65536) & 0xffff;
    }

    public void setMemoryByte(int addr, int value) {
        addr = normalizeMemoryAddress(addr);
        memory[addr] = (byte) (value & 0xff);
    }

    public DebugLineInfo getDebugInfo(int ip) {
        return debugInfo.get(ip);
    }

    public String getContext() {
        return context;
    }

    public void setParityFlags() {
        int v = getA() & 0xff;
        int count1s = 0;
        while (v != 0) {
            if ((v & 0x1) == 1) {
                ++count1s;
            }
            v = v >> 1;
        }
        parity = count1s % 2 == 0 ? true : false;
    }

    public void incrementRegisterPair(Operand op) {
        int value = 0;
        switch (op) {
        case B:
            value = getBC() + 1;
            setBC(value);
            break;
        case D:
            value = getDE() + 1;
            setDE(value);
            break;
        case H:
            value = getHL() + 1;
            setHL(value);
            break;
        case M:
        case SP:
            // special case we treat M as SP
            value = getSP() + 1;
            setSP(value);
            break;
        default:
            throw new IllegalStateException("Invalid register pair specified " + op.toString());
        }
    }

    public void decrementRegisterPair(Operand op) {
        int value = 0;
        switch (op) {
        case B:
            value = getBC() - 1;
            setBC(value);
            break;
        case D:
            value = getDE() - 1;
            setDE(value);
            break;
        case H:
            value = getHL() - 1;
            setHL(value);
            break;
        case M:
        case SP:
            // special case we treat M as SP
            value = getSP() - 1;
            setSP(value);
            break;
        default:
            throw new IllegalStateException("Invalid register pair specified " + op.toString());
        }
    }

    /**
     * convert from integer opcode to Operand
     * 
     * @param i
     * @return
     */
    public Operand toOperand(int i) {
        switch (i) {
        case 0:
            return Operand.B;
        case 1:
            return Operand.C;
        case 2:
            return Operand.D;
        case 3:
            return Operand.E;
        case 4:
            return Operand.H;
        case 5:
            return Operand.L;
        case 6:
            return Operand.M;
        case 7:
            return Operand.A;
        default:
            throw new IllegalStateException("Invalid register index in " + i);
        }
    }
}
