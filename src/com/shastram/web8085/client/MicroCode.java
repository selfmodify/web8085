package com.shastram.web8085.client;

import java.util.logging.Logger;

import com.shastram.web8085.client.Instruction.OneInstruction;
import com.shastram.web8085.client.InstructionParser.Operand;

/**
 * 
 * All instructions derive from here
 * 
 */
public abstract class MicroCode {
    private static Logger log = Logger.getLogger(MicroCode.class.getName());

    public abstract void execute(Exe exe, OneInstruction i) throws Exception;

    public static MicroCode nop = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            exe.hltExecuted = true;
        }
    };

    private static void assertInternal(Exe exe, OneInstruction i) throws Exception {
        int ip = exe.getIp();
        String line = exe.getAsertionAt(ip);
        // split the assertions into its parts
        if (line == null) {
            log.info("nothing to assert at ip=" + ip);
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
                compare(exe, num, exe.getSign(), "Expected Sign=");
            } else if ("z".equals(lhs)) {
                compare(exe, num, exe.getZero(), "Expected Zero=");
            } else if ("cy".equals(lhs)) {
                compare(exe, num, exe.getCarry(), "Expected Carry=");
            } else if ("ac".equals(lhs)) {
                compare(exe, num, exe.getAuxCarry(), "Expected AuxCarry=");
            } else if ("p".equals(lhs)) {
                compare(exe, num, exe.getParity(), "Expected Parity=");
            } else if (lhs.startsWith("[")) {
                // parse [<memory address>]=number
                String n = lhs.replaceAll("\\[", "").replaceAll("\\]", "");
                int addr = OperandParser.parseNumber(n);
                int value = exe.getMemory(addr);
                compare(exe, value, num, "Expected memory at address [" + addr + "] = ");
            } else {
                // try to parse it as a register. 
                op = OperandParser.parseRegisterForAssert(lhs);
                String msg = "Expected " + op.toString() + "=";
                int got = exe.getRegOrMem(op);
                compare(exe, num, got, msg);
            }
        }
        if (Config.printAssertions) {
            log.info("Assertion passed: " + line);
        }
        exe.nextIp();
    }

    private static void compare(Exe exe, int expected, int got, String msg) throws Exception {
        if (expected != got) {
            exe.assertionFailed(msg + Integer.toHexString(expected) + "h Got=" + Integer.toHexString(got) + "h");
        }
    }

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
            log.info("HLT executed. " + exe.getRegisterValues());
        }
    };

    protected static void addWithoutCarry(Exe exe, int op1) {
        endAdd(exe, op1, (short) 0, true);
    }

    public static MicroCode adi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0xC6);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            addWithCarry(exe, op1);
        }
    };

    public void addWithCarry(Exe exe, int op1) {
        endAdd(exe, op1, exe.getCarry(), true);
    }

    public static MicroCode aci = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0xce);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            addWithCarry(exe, op1);
        }
    };

    public static MicroCode inra = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert (exe.getOpcode() == 0x3C);
            endAdd(exe, 1, (short) 0, false);
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
            int addr = exe.read16bit();
            exe.h = exe.getMemory(addr);
            exe.l = exe.getMemory(addr + 1);
        }
    };

    public static MicroCode shld = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.read16bit();
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
            int addr = exe.read16bit();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.a = exe.getMemory(addr);
        }
    };

    public static MicroCode ldax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x0A ? exe.readBc() : exe.readDe();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.a = exe.getMemory(addr);
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
            int addr = exe.read16bit();
            exe.setMemoryByte(addr, (byte) exe.a);
        }
    };

    public static MicroCode stax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x02 ? exe.readBc() : exe.readDe();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.setMemoryByte(addr, (byte) exe.a);
        }
    };

    public static MicroCode lxi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int value = exe.read16bit();
            int low = value & 0xff;
            int high = (value & 0xff00) >> 8;
            switch (code) {
            case 0x1:
                exe.b = high;
                exe.c = low;
                break;
            case 0x11:
                exe.d = high;
                exe.e = low;
                break;
            case 0x21:
                exe.h = high;
                exe.l = low;
                break;
            case 0x31:
                exe.sp = value;
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
            exe.setRegOrMem(h, Operand.D);
            exe.setRegOrMem(l, Operand.E);
            exe.setRegOrMem(d, Operand.H);
            exe.setRegOrMem(e, Operand.L);
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
            exe.setRegOrMem(h, Operand.H);
            exe.setRegOrMem(l, Operand.L);
        }
    };

    /**
     * helper add function.
     * 
     * @param exe
     * @param r
     */
    private static void endAdd(Exe exe, int v, short carry) {
        endAdd(exe, v, carry, true /* set carry*/);
    }

    /**
     * Do a = a+v, set Zero, Sign, set carry optionally and increment the ip.
     * 
     * @param exe
     * @param v
     * @param setCarry
     */
    private static void endAdd(Exe exe, int v, short carry, boolean setCarry) {
        int r = exe.a + v + carry;
        exe.a = (0xff & r);
        if (setCarry) {
            if (r > 0xff) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
        }
        exe.setZSFlags();
        exe.setParityFlags();
        exe.nextIp();
    }
}
