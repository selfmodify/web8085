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
            int op1 = code % 8;
            int r = exe.a + exe.getRegOrMem(op1);
            endAdd(exe, r);
        }
    };

    public static MicroCode adi = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            assert(exe.getOpcode() == 0xC6);
            exe.nextIp();
            int op1 = exe.getMemAtIp();
            int r = exe.a + op1;
            endAdd(exe, r);
        }
    };

    private static void endAdd(Exe exe, int r) {
        exe.a = (0xff & r);
        if(r > 0xff) {
            exe.setCarry();
        } else {
            exe.resetCarry();
        }
        exe.setZSFlags();
        exe.nextIp();
    }
}
