package com.shastram.web8085.client;

import java.util.logging.Logger;

import com.shastram.web8085.client.Parser.Operand;

/**
 * 
 * All instructions microcode are implemented here.
 * 
 */
public abstract class MicroCode {
    private static Logger logger = Logger.getLogger(MicroCode.class.getName());

    /**
     * Represents a runtime instructions with its shortname, longname, length
     * and a its implementation.
     * 
     * @author vijay
     * 
     */
    public static class OneInstruction {
        public String shortName;
        public String longName;
        public int opcode;
        public int length;
        private final InstructionToString toStr;
        private final MicroCode microCode;

        public OneInstruction(int opcode, String shortName, String longName, InstructionToString toStr,
                MicroCode microCode) {
            this.shortName = shortName;
            this.longName = longName;
            this.opcode = opcode;
            this.toStr = toStr;
            this.microCode = microCode;
            if (toStr == wordOperand) {
                this.length = 3;
            } else if (toStr == byteOperand) {
                this.length = 2;
            } else {
                this.length = 1;
            }
        }

        public String toString(int op1) {
            return toStr.toString(longName, op1);
        }

    }

    public static interface InstructionToString {
        public String toString(String name, int op1);
    }

    public static class NoOperand implements InstructionToString {
        @Override
        public String toString(String name, int op1) {
            return name;
        }
    }

    public static class ByteOperand implements InstructionToString {
        @Override
        public String toString(String name, int op1) {
            return name + " " + op1;
        }
    }

    public static class WordOperand implements InstructionToString {
        @Override
        public String toString(String name, int op1) {
            return name + " " + op1;
        }
    }

    static NoOperand noOperand = new NoOperand();

    static ByteOperand byteOperand = new ByteOperand();
    static WordOperand wordOperand = new WordOperand();

    public static String toString(int opcode, int op1) {
        String str = opcode >= 0 && opcode < MicrocodeTable.table.length ? MicrocodeTable.table[opcode].toString(op1)
                : "";
        return str;
    }

    public static void execute(Exe exe) throws Exception {
        int opcode = exe.getMemAtIp();
        if (opcode < 0 || opcode >= MicrocodeTable.table.length) {
            throw new Exception("Invalid opcode " + opcode + " at ip " + exe.getIp());
        }
        OneInstruction ix = MicrocodeTable.table[opcode];
        MicroCode m = ix.microCode;
        if (Config.printInstructions) {
            String s = ix.longName;
            DebugLineInfo debugInfo = exe.getDebugInfo(exe.ip);
            logger.info("Executing " + s + " " + (debugInfo != null ? "Line=" + debugInfo.line : ""));
        }
        m.execute(exe, ix);
    }

    public static MicroCode ana = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0xa0;
            int op1 = exe.getRegOrMem(code);
            doAnd(exe, op1);
            exe.nextIp();
        }
    };

    public static MicroCode ani = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            doAnd(exe, exe.getMemAtIp());
            exe.nextIp();
        }
    };

    public static MicroCode cma = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int value = (~exe.getA()) & 0xff;
            exe.setA(value);
            exe.nextIp();
        }
    };

    public static MicroCode cmp = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0xb8;
            int op1 = exe.getRegOrMem(code);
            doCompare(exe, op1);
        }
    };

    public static MicroCode cpi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            doCompare(exe, exe.getMemAtIp());
        }
    };

    private static void doCompare(Exe exe, int value) {
        value = value & 0xff;
        doSubtract(exe, value, 0, false, exe.getA(), true);
        int a = exe.getA();
        if (a < value) {
            exe.setCarry();
            exe.resetZero();
        } else if (a == value) {
            exe.setZero();
            exe.resetCarry();
        } else {
            exe.resetCarry();
            exe.resetZero();
        }
    }

    public static MicroCode ora = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0xb0;
            int op1 = exe.getRegOrMem(code);
            doOr(exe, op1);
            exe.nextIp();
        }
    };

    public static MicroCode ori = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int op1 = exe.getMemAtIp();
            doOr(exe, op1);
            exe.nextIp();
        }
    };

    public static MicroCode xra = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0xa8;
            int op1 = exe.getRegOrMem(code);
            doXOr(exe, op1);
            exe.nextIp();
        }
    };

    public static MicroCode xri = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int op1 = exe.getMemAtIp();
            doXOr(exe, op1);
            exe.nextIp();
        }
    };

    public static MicroCode nop = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            exe.hltExecuted = true;
        }
    };

    /**
     * parse and execute assertions ".assert a=1" => reg a must be equal to 1
     * .assert cy=1 => carry flag must be 1
     */
    public static MicroCode assertRunner = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int ip = exe.getIp();
            try {
                assertInternal(exe, i);
            } catch (Exception e) {
                String msg =
                        "Assert failed at "
                                + exe.getContext()
                                + ":" + exe.getDebugInfo(ip).line
                                + ": " + e.getMessage();
                throw new Exception(msg);
            }
        }
    };

    public static MicroCode breakRunner = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.showDialog("At breakpoint");
        }

    };

    /**
     * Move instruction
     */
    public static MicroCode move = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x40;
            int op1 = code / 8;
            int op2 = code % 8;
            int value = exe.getRegOrMem(op2);
            exe.setRegOrMem(op1, value);
            exe.nextIp();
        }
    };

    public static MicroCode mvi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x06;
            int op1 = code / 8;
            exe.nextIp(); // one for the immediate operand
            int data = exe.getMemAtIp();
            exe.setRegOrMem(op1, data);
            exe.nextIp();
        }
    };

    /**
     * Add instruction family.
     */
    public static MicroCode add = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x80;
            int op1 = exe.getRegOrMem(code % 8);
            addWithoutCarry(exe, op1);
        }
    };

    public static MicroCode adc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x88;
            int op1 = exe.getRegOrMem(code % 8);
            addWithCarry(exe, op1);
        }
    };

    public static MicroCode hlt = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.hltExecuted = true;
            if (Config.showHltExecuted) {
                logger.info("HLT executed. " + exe.getRegisterValues());
            }
        }
    };
    public static MicroCode adi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0xC6);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            addWithCarry(exe, op1);
        }
    };

    public static MicroCode aci = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0xce);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            addWithCarry(exe, op1);
        }
    };

    public static MicroCode cmc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            if (exe.getCarry() == 1) {
                exe.resetCarry();
            } else {
                exe.setCarry();
            }
            exe.nextIp();
        }
    };

    public static MicroCode dad = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x09;
            int op1 = exe.getRegOrMemPair(code / 8);
            int finalValue = exe.getHL() + op1;
            if (finalValue > 0xffff) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
            exe.setHL(finalValue);
            exe.nextIp();
        }
    };

    public static MicroCode dcr = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x05;
            int op1 = exe.getRegOrMem(code / 8);
            boolean setAuxCarry = (op1 == Operand.A.ordinal());
            int finalValue = doSubtract(exe, 1, 0, false, op1, setAuxCarry);
            exe.setRegOrMem(code / 8, finalValue);
        }
    };

    public static MicroCode dcx = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x0B;
            Operand op = exe.toOperand(code / 8);
            exe.decrementRegisterPair(op);
            exe.nextIp();
        }
    };

    public static MicroCode sub = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x90;
            int op1 = exe.getRegOrMem(code % 8);
            doSubtractAndSetRegisterA(exe, op1, 0);
        }
    };

    public static MicroCode sui = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            doSubtractAndSetRegisterA(exe, op1, 0);
        }
    };

    public static MicroCode sbb = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x98;
            int op1 = exe.getRegOrMem(code % 8);
            doSubtractAndSetRegisterA(exe, op1, exe.getCarry());
        }
    };

    public static MicroCode sbi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            int finalValue = doSubtract(exe, op1, exe.getCarry(), true, exe.getA(), true);
            exe.setA(finalValue);
        }
    };

    /**
     * Rotate accumulator left through carry. Each binary bit of the accumulator
     * is rotated left by one position through the carry flag. Bit D7 is placed
     * in the bit in the carry flag and carry flag is placed in the least
     * significant position D0.
     */
    public static MicroCode ral = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int value = (exe.getA() & 0xff) << 1;
            int aValue = (value | exe.getCarry()) & 0xff;
            exe.setA(aValue);
            if ((value & 0x100) == 0x100) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
            exe.nextIp();
        }
    };

    /**
     * Each binary bit of the accumulator is rotated left by one position. Bit
     * D7 is placed in the position of D0 as well as in the Carry flag.
     */
    public static MicroCode rlc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int value = (exe.getA() & 0xff) << 1;
            int lsb = value >> 8;
            value = (value | lsb) & 0xff;
            if ((lsb & 0x1) == 1) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
            exe.setA(value);
            exe.nextIp();
        }
    };

    /**
     * RAR - Rotate accumulator right through carry. Each binary bit of the
     * accumulator is rotated right by one position through the carry flag. Bit
     * D0 is placed in the carry flag and the bit in the carry flag is placed in
     * the most significant position, D7.
     */
    public static MicroCode rar = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int aValue = exe.getA() & 0xff;
            int lsb = aValue & 0x1;
            exe.setA((aValue >> 1) | (exe.getCarry() << 8));
            if (lsb == 1) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
            exe.nextIp();
        }
    };

    /**
     * Each binary bit of the accumulator is rotated right by one position. Bit
     * D0 is placed in the position of D7 as well as in the carry flag.
     */
    public static MicroCode rrc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int aValue = exe.getA() & 0xff;
            int value = aValue & 1;
            if ((value & 0x1) == 1) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
            exe.setA((aValue >> 1) | (value << 7));
            exe.nextIp();
        }
    };

    public static MicroCode inr = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x04;
            doAdd(exe, 1, 0, false, code / 8);
        }
    };

    public static MicroCode inx = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x03;
            Operand op = exe.toOperand(code / 8);
            exe.incrementRegisterPair(op);
            exe.nextIp();
        }
    };

    public static MicroCode stc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0x37);
            exe.setCarry();
            exe.nextIp();
        }
    };

    public static MicroCode lhld = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.getMemAtIp16bit();
            exe.h = exe.getMemory(addr);
            exe.l = exe.getMemory(addr + 1);
        }
    };

    public static MicroCode shld = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.getMemAtIp16bit();
            exe.setMemoryByte(addr, exe.l);
            exe.setMemoryByte(addr + 1, exe.h);
        }
    };

    // The contents of a memory location, specified by a 16-bit address in the operand
    // are copied to the accumulator
    public static MicroCode lda = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.getMemAtIp16bit();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.setA(exe.getMemory(addr));
        }
    };

    public static MicroCode ldax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x0A ? exe.getBC() : exe.getDE();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.setA(exe.getMemory(addr));
        }
    };

    public static MicroCode jmp = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int location = exe.getMemAtIp16bit();
            exe.setIp(location);
        }
    };

    public static MicroCode call = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int location = exe.getMemAtIp16bit();
            push16BitToStack(exe, exe.getIp());
            exe.setIp(location);
        }
    };
    /**
     * The contents of the accumulator are stored in the memory location
     * specified
     */
    public static MicroCode sta = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.getMemAtIp16bit();
            exe.setMemoryByte(addr, (byte) exe.getA());
        }
    };

    public static MicroCode stax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x02 ? exe.getBC() : exe.getDE();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.setMemoryByte(addr, (byte) exe.getA());
        }
    };

    public static MicroCode lxi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int value = exe.getMemAtIp16bit();
            int low = value & 0xff;
            int high = (value & 0xff00) >> 8;
            switch (code) {
            case 0x1:
                exe.setBC(value);
                break;
            case 0x11:
                exe.setDE(value);
                break;
            case 0x21:
                exe.setHL(value);
                break;
            case 0x31:
                exe.setSP(value);
                break;
            default:
                throw new Exception("Invalid microcode " + code + " interpreted as lxi.");
            }
        }
    };

    /**
     * XCHG Exchange H & L with D & E
     */
    public static MicroCode xchg = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int h = exe.getRegOrMem(Operand.H);
            int l = exe.getRegOrMem(Operand.L);
            int d = exe.getRegOrMem(Operand.D);
            int e = exe.getRegOrMem(Operand.E);
            exe.setRegOrMem(Operand.D, h);
            exe.setRegOrMem(Operand.E, l);
            exe.setRegOrMem(Operand.H, d);
            exe.setRegOrMem(Operand.L, e);
        }
    };

    /**
     * XTHL Exchange Top of Stack with H & L
     */
    public static MicroCode xthl = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int sp = exe.getRegOrMem(Operand.SP);
            int h = exe.getMemory(sp);
            int l = exe.getMemory(sp + 1);
            exe.setRegOrMem(Operand.H, h);
            exe.setRegOrMem(Operand.L, l);
        }
    };

    protected static void addWithoutCarry(Exe exe, int op1) {
        doAdd(exe, op1, (short) 0, true);
    }

    private static void assertInternal(Exe exe, OneInstruction i) throws Exception {
        int ip = exe.getIp();
        String line = exe.getAsertionAt(ip);
        // split the assertions into its parts
        if (line == null) {
            logger.info("nothing to assert at ip=" + ip);
            return;
        }

        String[] parts = line.split(",");
        for (String s : parts) {
            // get the first assertion
            String assertion1 = s.trim().toLowerCase(); //.replaceAll(",", "");
            // get the expression
            String[] p = assertion1.split("=", 2);
            if (p.length < 2) {
                exe.showDialog("Invalid assertion " + assertion1);
                continue;
            }

            // parse the number specified in the assertion
            int num = OperandParser.parseNumber(p[1].trim());

            // assert sign
            String lhs = p[0].trim().toLowerCase();
            Operand op;
            if ("s".equals(lhs)) {
                assertCompare(exe, num, exe.getSign(), "Expected Sign=");
            } else if ("z".equals(lhs)) {
                assertCompare(exe, num, exe.getZero(), "Expected Zero=");
            } else if ("cy".equals(lhs)) {
                assertCompare(exe, num, exe.getCarry(), "Expected Carry=");
            } else if ("ac".equals(lhs)) {
                assertCompare(exe, num, exe.getAuxCarry(), "Expected AuxCarry=");
            } else if ("p".equals(lhs)) {
                assertCompare(exe, num, exe.getParity(), "Expected Parity=");
            } else if (lhs.startsWith("[")) {
                // parse [<memory address>]=number
                String n = lhs.replaceAll("\\[", "").replaceAll("\\]", "");
                int addr = OperandParser.parseNumber(n);
                int value = exe.getMemory(addr);
                assertCompare(exe, num, value, "Expected memory at address [" + Integer.toHexString(addr) + "h] = ");
            } else {
                // try to parse it as a register. 
                op = OperandParser.parseRegisterForAssert(lhs);
                String msg = "Expected " + op.toString() + "=";
                int got = exe.getRegOrMem(op);
                assertCompare(exe, num, got, msg);
            }
        }
        if (Config.printAssertions) {
            logger.info("Assertion passed at ip=" + exe.ip + " " + line);
        }
        exe.nextIp();
    }

    private static void assertCompare(Exe exe, int expected, int got, String msg) throws Exception {
        if (expected != got) {
            exe.assertionFailed(msg + Integer.toHexString(expected) + "h Got=" + Integer.toHexString(got) + "h");
        }
    }

    /**
     * Do a = a+v, set Zero, Sign, set carry optionally and increment the ip.
     * 
     * @param exe
     * @param v
     * @param setCarry
     */
    private static void doAdd(Exe exe, int v, int carry, boolean setCarry) {
        doAdd(exe, v, carry, setCarry, Operand.A.ordinal());
    }

    /**
     * perform the add operation
     * 
     * @param exe
     *            : executable context
     * @param v
     *            : The value to be added
     * @param carry
     *            : The carry/borrow to be added
     * @param setCarry
     *            : Should the carry flag be set after this operation is done
     * @param register
     *            : Which register should be set? Typically this is the
     *            accumulator but it can be other reg/mem in case of inr
     */
    private static void doAdd(Exe exe, int v, int carry, boolean setCarry, int regOrMem) {
        int regOrMemValue = exe.getRegOrMem(regOrMem);
        carry = carry & 0x1;
        int other = v + carry;
        int finalValue = regOrMemValue + other;

        if (regOrMem == Operand.A.ordinal()) {
            // find if there is aux carry -
            // This flag is set to a 1 by the instruction just ending
            // if a carry occurred from bit 3 to bit 4 of the A Register
            // during the instructions execution
            exe.setAuxCarry(0xff & other);
        }
        exe.setRegOrMem(regOrMem, finalValue);
        if (setCarry) {
            if (finalValue > 0xff) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
        }
        exe.setZSFlags(exe.getA());
        exe.setParityFlags(exe.getA());
        exe.nextIp();
    }

    private static void doAnd(Exe exe, int value) {
        value = value & 0xff;
        exe.setA(exe.getA() & value);
        exe.setZSFlags(exe.getA());
        exe.setParityFlags(exe.getA());

        exe.resetCarry();
        exe.setAuxCarry();
    }

    private static void doOr(Exe exe, int value) {
        value = value & 0xff;
        exe.setA(exe.getA() | value);
        exe.setZSFlags(exe.getA());
        exe.setParityFlags(exe.getA());

        exe.resetCarry();
        exe.resetAuxCarry();
    }

    private static void doXOr(Exe exe, int value) {
        value = value & 0xff;
        exe.setA(exe.getA() ^ value);
        exe.setZSFlags(exe.getA());
        exe.setParityFlags(exe.getA());

        exe.resetCarry();
        exe.resetAuxCarry();
    }

    /**
     * Only use for subtract/dcr instructions. Do not use for compare as this
     * will set the value of the register A. Carry is always set. A = A - v
     * 
     * @param exe
     * @param other
     *            - operand1
     * @param carry
     *            - The value of the carry 0/1
     */
    private static void doSubtractAndSetRegisterA(Exe exe, int other, int carry) {
        int finalValue = doSubtract(exe, other, carry, true /* set carry */, exe.getA(), true);
        exe.setA(0xff & finalValue);
    }

    /**
     * Do subtraction without affecting any registers. Cy / AuxCy may be set
     * depending on the parameters passed.
     * 
     * @param exe
     * @param v
     *            - operand1
     * @param carry
     *            - The value of the carry 0/1
     * @param setCarry
     *            - Should the carry flag be set after the operation.
     * @param regOrMemValue
     *            - operand
     * @param setAuxCarry
     *            - Should the aux carry be set based on the result.
     * @return: ( 2's complement of (v + carry)) + op1
     */
    private static int doSubtract(Exe exe, int v, int carry, boolean setCarry, int regOrMemValue,
            boolean setAuxCarry) {
        // 2's complement
        carry = carry & 0x1;
        v = (v & 0xff) + carry;
        int other = ((~v) + 1) & 0xff;
        int finalValue = regOrMemValue + other;

        if (setAuxCarry) {
            // find if there is aux carry -
            // This flag is set to a 1 by the instruction just ending
            // if a carry occurred from bit 3 to bit 4 of the A Register
            // during the instructions execution
            exe.setAuxCarry(0xff & other);
        }
        if (setCarry) {
            if (finalValue > 0xff) {
                exe.resetCarry();
            } else {
                exe.setCarry();
            }
        }
        exe.setZSFlags(finalValue);
        exe.setParityFlags(finalValue);
        exe.nextIp();
        finalValue = finalValue & 0xff;
        return finalValue;
    }

    protected void addWithCarry(Exe exe, int op1) {
        doAdd(exe, op1, exe.getCarry(), true);
    }

    /**
     * push 16 bit value to the top of stack and decrement stack pointer push
     * 2013 will do the following [sp--] = 20, [sp--] = 13
     * 
     * @param value
     */
    protected void push16BitToStack(Exe exe, int value) {
        exe.setMemoryByte(exe.getSP(), (value >> 8));
        exe.decrementRegisterPair(Operand.SP);
        exe.setMemoryByte(exe.getSP(), (value & 0xf));
        exe.decrementRegisterPair(Operand.SP);
    }

    public abstract void execute(Exe exe, OneInstruction i) throws Exception;

    public static void selfTest() {
        for (int i = 0; i < MicrocodeTable.table.length; ++i) {
            OneInstruction ix = MicrocodeTable.table[i];
            logger.info(ix.shortName + " code=" + ix.microCode);
        }
    }
}
