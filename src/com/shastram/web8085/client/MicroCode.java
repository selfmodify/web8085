package com.shastram.web8085.client;

import java.util.logging.Logger;

import com.shastram.web8085.client.Instruction.OneInstruction;

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

    /**
     * parse and execute assertions ".assert a=1" => reg a must be equal to 1
     * .assert cy=1 => carry flag must be 1
     */
    public static MicroCode assertRunner = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            String line = exe.getAsertionAt(exe.getIp());
            // split the assertions into its parts
            if(line == null) {
            	log.info("nothing to assert ");
            	return;
            }
            String[] parts = line.split("[ \t]+");
            for(String s: parts) {
                // get the first assertion
                String assertion1 = s.trim().toLowerCase().replaceAll(",", "");
                // get the expression
                String[] p = assertion1.split("=",2);
                if(p.length < 2) {
                    exe.showDialog("Invalid assertion " + assertion1);
                    continue;
                }

                // parse the number specified in the assertion
                int num = 0;
                try {
                    num = Integer.parseInt(p[1].trim());
                } catch(NumberFormatException e) {
                    exe.showDialog("Invalid number in expression " + assertion1);
                }

                // assert sign
                String lhs = p[0].trim().toLowerCase();
                if("s".equals(lhs)) {
                    compare(exe, num, exe.getSign(), "Expected Sign=");
                } else if("z".equals(lhs)) {
                    compare(exe, num, exe.getZero(), "Expected Zero=");
                } else if("cy".equals(lhs)) {
                    compare(exe, num, exe.getCarry(), "Expected Carry=");
                } else if("ac".equals(lhs)) {
                    compare(exe, num, exe.getAuxCarry(), "Expected AuxCarry=");
                }
            }
            log.info("Assertion passed: " + line);
        }

        private void compare(Exe exe, int expected, int got, String msg) throws Exception {
            if(expected != got) {
                exe.assertionFailed(msg + expected + " Got="+got);
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

    public static MicroCode hlt = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.hltExecuted = true;
            log.info("HLT executed");
        }
    };

    protected static void addWithoutCarry(Exe exe, int op1) {
        endAdd(exe, op1, (short) 0);
    }

    public static MicroCode adi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0xC6);
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
            assert(exe.getOpcode() == 0xce);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            addWithCarry(exe, op1);
        }
    };

    public static MicroCode inra = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0x3C);
            endAdd(exe, 1, (short)0, false);
        }
    };

    public static MicroCode stc = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0x37);
            exe.setCarry();
            exe.nextIp();
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
            exe.a = exe.memory[addr];
        }
    };

    public static MicroCode ldax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x0A ? exe.readBc() : exe.readDe();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.a = exe.memory[addr];
        }
    };

    /**
     * The contents of the accumulator are stored in the memory location specified
     */
    public static MicroCode sta = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.read16bit();
            exe.memory[addr] = (byte)exe.a;
        }
    };

    public static MicroCode stax = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getMemAtIp();
            exe.nextIp();
            int addr = code == 0x02 ? exe.readBc() : exe.readDe();
            // set the value of the accumulator with the value of memory at 'addr'
            exe.memory[addr] = (byte)exe.a;
        }
    };


    /**
     * helper add function.
     * @param exe
     * @param r
     */
    private static void endAdd(Exe exe, int v, short carry) {
        endAdd(exe, v, carry, true /* set carry*/);
    }

    /**
     * Do a = a+v, set Zero, Sign, set carry optionally and
     * increment the ip.
     * @param exe
     * @param v
     * @param setCarry
     */
    private static void endAdd(Exe exe, int v, short carry, boolean setCarry) {
        int r = exe.a + v;
        exe.a = (0xff & r);
        if(setCarry) {
            if(r > 0xff) {
                exe.setCarry();
            } else {
                exe.resetCarry();
            }
        }
        exe.setZSFlags();
        exe.nextIp();
    }
}
