package com.shastram.web8085.client;

import java.util.HashMap;

import com.shastram.web8085.client.Instruction.OneInstruction;

/**
 *
 * All instructions derive from here
 *
 */
public abstract class MicroCode {
    public abstract void execute(Exe exe, OneInstruction i) throws Exception;

    public static MicroCode nop = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
        }
    };

    public static MicroCode assertRunner = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            String line = exe.getAsertionAt(exe.getIp());
            // split the assertions into its parts
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
                int num = 0;
                try {
                    num = Integer.parseInt(p[1].trim());
                } catch(NumberFormatException e) {
                    exe.showDialog("Invalid number in expression " + assertion1);
                }

                if("s".equalsIgnoreCase(p[0].trim())) {
                    if(exe.getSign() != num) {
                        exe.assertionFailed("Expected Sign=" + exe.getSign() + " Got="+num);
                    }
                }
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

    /**
     * helper add function.
     * @param exe
     * @param r
     */
    private static void endAdd(Exe exe, int v, short carry) {
        endAdd(exe, v, carry, true);
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
