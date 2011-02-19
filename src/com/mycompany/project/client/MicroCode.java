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
    
    public static MicroCode move = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
        }
    };
}
