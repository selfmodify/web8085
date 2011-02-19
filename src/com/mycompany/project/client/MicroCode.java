package com.mycompany.project.client;

/**
 * 
 * All instructions derive from here
 *
 */
public abstract class MicroCode {
    public abstract void execute(Exe exe, Instruction i) throws Exception;

    public static MicroCode nop = new MicroCode() {
        @Override
        public void execute(Exe exe, Instruction i) throws Exception {
            exe.nextIp();
        }
    };
}
