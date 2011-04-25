package com.mycompany.project.client;

import com.mycompany.project.client.Instruction.OneInstruction;

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
            endAdd(exe, op1);
        }
    };

    public static MicroCode adi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0xC6);
            exe.nextIp(); // one for the immediate operand
            int op1 = exe.getMemAtIp();
            endAdd(exe, op1);
        }
    };

    public static MicroCode inra = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0x3C);
            endAdd(exe, 1, false);
        }
    };

    /**
     * helper add function.
     * @param exe
     * @param r
     */
    private static void endAdd(Exe exe, int v) {
        endAdd(exe, v, true);
    }

    /**
     * Do a = a+v, set Zero, Sign, set carry optionally and
     * increment the ip.
     * @param exe
     * @param v
     * @param setCarry
     */
    private static void endAdd(Exe exe, int v, boolean setCarry) {
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

    /**
     * Subtract instruction family.
     */
    public static MicroCode sub = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            int code = exe.getOpcode() - 0x80;
            int op1 = exe.getRegOrMem(code % 8);
            endAdd(exe, op1);
        }
    };
}
