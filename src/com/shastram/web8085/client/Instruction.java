package com.shastram.web8085.client;

import org.mortbay.log.Log;


public class Instruction {
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

    private static NoOperand noOperand = new NoOperand();

    private static ByteOperand byteOperand = new ByteOperand();
    private static WordOperand wordOperand = new WordOperand();

    public static class OneInstruction {
        public String shortName;
        public String longName;
        public int opcode;
        public int length;
        private InstructionToString toStr;
        private MicroCode microCode;

        public OneInstruction(String shortName, String longName, InstructionToString toStr, MicroCode microCode) {
            this.shortName = shortName;
            this.longName = longName;
            this.toStr = toStr;
            this.microCode = microCode;
            if(toStr == wordOperand) {
                this.length = 3;
            } else if(toStr == byteOperand){
                this.length = 2;
            } else {
                this.length = 1;
            }
        }

        public String toString(int op1) {
            return toStr.toString(longName, op1);
        }

    }

    public static OneInstruction[] allInstructions = new OneInstruction[] {
            // 0 thru 10
            new OneInstruction("nop", "nop",  noOperand ,MicroCode.nop),
            new OneInstruction("lxi", "lxi b", wordOperand ,MicroCode.nop),
            new OneInstruction("stax", "stax b",  noOperand ,MicroCode.nop),
            new OneInstruction("inx", "inx b",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr b",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr b",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi b", byteOperand ,MicroCode.nop),
            new OneInstruction("rlc", "rlc",  noOperand ,MicroCode.nop),
            new OneInstruction("", "assert",  noOperand ,MicroCode.assertRunner),
            new OneInstruction("dad", "dad b",  noOperand ,MicroCode.nop),
            new OneInstruction("ldax", "ldax b",  noOperand ,MicroCode.nop),

            // 11 thru 20
            new OneInstruction("dcx", "dcx b",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr c",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr c",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi c", byteOperand ,MicroCode.nop),
            new OneInstruction("rrc", "rrc",  noOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("lxi", "lxi d", wordOperand ,MicroCode.nop),
            new OneInstruction("stax", "stax d",  noOperand ,MicroCode.nop),
            new OneInstruction("inx", "inx d",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr d",  noOperand ,MicroCode.nop),
            //21
            new OneInstruction("dcr", "dcr d",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi d", byteOperand ,MicroCode.nop),
            new OneInstruction("ral", "ral",  noOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("dad", "dad d",  noOperand ,MicroCode.nop),
            new OneInstruction("ldax", "ldax d",  noOperand ,MicroCode.nop),
            new OneInstruction("dcx", "dcx d",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr e",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr e",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi e",  byteOperand ,MicroCode.nop),
            //31
            new OneInstruction("rar", "rar",  noOperand ,MicroCode.nop),
            new OneInstruction("rim", "rim",  noOperand ,MicroCode.nop),
            new OneInstruction("lxi", "lxi h",  wordOperand ,MicroCode.nop),
            new OneInstruction("shld", "shld",  wordOperand ,MicroCode.nop),
            new OneInstruction("inx", "inx h",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr h",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr h",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi h",  byteOperand ,MicroCode.nop),
            new OneInstruction("daa", "daa",  noOperand ,MicroCode.nop),
            //41
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("dad", "dad h",  noOperand ,MicroCode.nop),
            new OneInstruction("lhld", "lhld",  wordOperand ,MicroCode.nop),
            new OneInstruction("dcx", "dcx h",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr l",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr l",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi l",  byteOperand ,MicroCode.nop),
            new OneInstruction("cma", "cma",  noOperand ,MicroCode.nop),
            new OneInstruction("sim", "sim",  noOperand ,MicroCode.nop),
            new OneInstruction("lxi", "lxi sp",  wordOperand ,MicroCode.nop),
            //51
            new OneInstruction("sta", "sta",  wordOperand ,MicroCode.nop),
            new OneInstruction("inx", "inx sp",  noOperand ,MicroCode.nop),
            new OneInstruction("inr", "inr m",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr m",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi m",  byteOperand ,MicroCode.nop),
            new OneInstruction("stc", "stc",  noOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("dad", "dad sp",  noOperand ,MicroCode.nop),
            new OneInstruction("lda", "lda",  wordOperand ,MicroCode.nop),
            new OneInstruction("dcx", "dcx sp",  noOperand ,MicroCode.nop),
            //61
            new OneInstruction("inr", "inr a",  noOperand ,MicroCode.nop),
            new OneInstruction("dcr", "dcr a",  noOperand ,MicroCode.nop),
            new OneInstruction("mvi", "mvi a",  byteOperand ,MicroCode.mvi),
            new OneInstruction("cmd", "cmd",  noOperand ,MicroCode.nop),

            //65
            new OneInstruction("mov", "mov b,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov b,a",  noOperand ,MicroCode.move),
            //73
            new OneInstruction("mov", "mov c,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov c,a",  noOperand ,MicroCode.move),
            //81
            new OneInstruction("mov", "mov d,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov d,a",  noOperand ,MicroCode.move),
            //89
            new OneInstruction("mov", "mov e,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov e,a",  noOperand ,MicroCode.move),
            //97
            new OneInstruction("mov", "mov h,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov h,a",  noOperand ,MicroCode.move),
            //105
            new OneInstruction("mov", "mov l,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov l,a",  noOperand ,MicroCode.move),
            //113 0x70
            new OneInstruction("mov", "mov m,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov m,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov m,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov m,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov m,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov m,l",  noOperand ,MicroCode.move),
            new OneInstruction("hlt", "hlt",  noOperand ,MicroCode.hlt),
            new OneInstruction("mov", "mov m,a",  noOperand ,MicroCode.move),
            //121
            new OneInstruction("mov", "mov a,b",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,c",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,d",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,e",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,h",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,l",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,m",  noOperand ,MicroCode.move),
            new OneInstruction("mov", "mov a,a",  noOperand ,MicroCode.move),

            new OneInstruction("add", "add b",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add c",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add d",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add e",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add h",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add l",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add m",  noOperand ,MicroCode.nop),
            new OneInstruction("add", "add a",  noOperand ,MicroCode.nop),

            new OneInstruction("adc", "adc b",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc c",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc d",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc e",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc h",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc l",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc m",  noOperand ,MicroCode.nop),
            new OneInstruction("adc", "adc a",  noOperand ,MicroCode.nop),

            new OneInstruction("sub", "sub b",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub c",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub d",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub e",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub h",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub l",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub m",  noOperand ,MicroCode.nop),
            new OneInstruction("sub", "sub a",  noOperand ,MicroCode.nop),

            new OneInstruction("sbb", "sbb b",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb c",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb d",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb e",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb h",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb l",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb m",  noOperand ,MicroCode.nop),
            new OneInstruction("sbb", "sbb a",  noOperand ,MicroCode.nop),

            new OneInstruction("ana", "ana b",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana c",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana d",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana d",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana h",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana l",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana m",  noOperand ,MicroCode.nop),
            new OneInstruction("ana", "ana a",  noOperand ,MicroCode.nop),

            new OneInstruction("xra", "xra b",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra c",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra d",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra d",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra h",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra l",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra m",  noOperand ,MicroCode.nop),
            new OneInstruction("xra", "xra a",  noOperand ,MicroCode.nop),

            new OneInstruction("ora", "ora b",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora c",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora d",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora d",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora h",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora l",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora m",  noOperand ,MicroCode.nop),
            new OneInstruction("ora", "ora a",  noOperand ,MicroCode.nop),

            new OneInstruction("cmp", "cmp b",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp c",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp d",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp d",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp h",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp l",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp m",  noOperand ,MicroCode.nop),
            new OneInstruction("cmp", "cmp a",  noOperand ,MicroCode.nop),

            new OneInstruction("rnz", "rnz",  noOperand ,MicroCode.nop),
            new OneInstruction("pop", "pop b",  noOperand ,MicroCode.nop),
            new OneInstruction("jnz", "jnz",  wordOperand ,MicroCode.nop),
            new OneInstruction("jmp", "jmp",  wordOperand ,MicroCode.nop),
            new OneInstruction("cnz", "cnz",  wordOperand ,MicroCode.nop),
            new OneInstruction("push", "push b",  noOperand ,MicroCode.nop),
            new OneInstruction("adi", "adi",  byteOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 0",  noOperand ,MicroCode.nop),
            new OneInstruction("rz", "rz",  noOperand ,MicroCode.nop),
            new OneInstruction("ret", "ret",  noOperand ,MicroCode.nop),
            new OneInstruction("jz", "jz",  wordOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("cz", "cz",  wordOperand ,MicroCode.nop),
            new OneInstruction("call", "call",  wordOperand ,MicroCode.nop),
            new OneInstruction("aci", "aci",  byteOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 1",  noOperand ,MicroCode.nop),
            new OneInstruction("rnc", "rnc",  noOperand ,MicroCode.nop),
            new OneInstruction("pop", "pop d",  noOperand ,MicroCode.nop),
            new OneInstruction("jnc", "jnc",  noOperand ,MicroCode.nop),
            new OneInstruction("out", "out",  noOperand ,MicroCode.nop),
            new OneInstruction("cnc", "cnc",  noOperand ,MicroCode.nop),
            new OneInstruction("push", "push",  noOperand ,MicroCode.nop),
            new OneInstruction("sui", "sui",  noOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 2",  noOperand ,MicroCode.nop),
            new OneInstruction("rc", "rc",  noOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("jc", "jc",  wordOperand ,MicroCode.nop),
            new OneInstruction("in", "in",  byteOperand ,MicroCode.nop),
            new OneInstruction("cc", "cc",  wordOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("sbi", "sbi",  noOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 3",  noOperand ,MicroCode.nop),
            new OneInstruction("rpo", "rpo",  noOperand ,MicroCode.nop),
            new OneInstruction("pop", "pop h",  noOperand ,MicroCode.nop),
            new OneInstruction("jpo", "jpo",  wordOperand ,MicroCode.nop),
            new OneInstruction("xthl", "xthl",  noOperand ,MicroCode.nop),
            new OneInstruction("cpo", "cpo",  wordOperand ,MicroCode.nop),
            new OneInstruction("push", "push h",  noOperand ,MicroCode.nop),
            new OneInstruction("ani", "ani",  wordOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 4",  noOperand ,MicroCode.nop),
            new OneInstruction("rep", "rep",  noOperand ,MicroCode.nop),
            new OneInstruction("pchl", "pchl",  noOperand ,MicroCode.nop),
            new OneInstruction("jpe", "jpe",  wordOperand ,MicroCode.nop),
            new OneInstruction("xchg", "xchg",  wordOperand ,MicroCode.nop),
            new OneInstruction("cpe", "cpe",  wordOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("xri", "xri",  noOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 5",  noOperand ,MicroCode.nop),
            new OneInstruction("rp", "rp",  noOperand ,MicroCode.nop),
            new OneInstruction("pop", "pop psw",  noOperand ,MicroCode.nop),
            new OneInstruction("jp", "jp",  wordOperand ,MicroCode.nop),
            new OneInstruction("di", "di",  noOperand ,MicroCode.nop),
            new OneInstruction("cp", "cp",  wordOperand ,MicroCode.nop),
            new OneInstruction("push", "push psw",  noOperand ,MicroCode.nop),
            new OneInstruction("ori", "ori",  byteOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 6",  noOperand ,MicroCode.nop),
            new OneInstruction("rm", "rm",  noOperand ,MicroCode.nop),
            new OneInstruction("sphl", "sphl",  noOperand ,MicroCode.nop),
            new OneInstruction("jm", "jm",  wordOperand ,MicroCode.nop),
            new OneInstruction("ei", "ei",  noOperand ,MicroCode.nop),
            new OneInstruction("cm", "cm",  wordOperand ,MicroCode.nop),
            new OneInstruction("", "invalid",  noOperand ,MicroCode.nop),
            new OneInstruction("cpi", "cpi",  byteOperand ,MicroCode.nop),
            new OneInstruction("rst", "rst 7",  noOperand ,MicroCode.nop),
    };

    public static String toString(int opcode, int op1) {
        String str = opcode >= 0 && opcode < allInstructions.length ? allInstructions[opcode].toString(op1) : "";
        return str;
    }

    public static void execute(Exe exe) throws Exception {
        int opcode = (int)exe.getMemAtIp();
        if(opcode < 0 || opcode >=allInstructions.length) {
            throw new Exception("Invalid opcode " + opcode + " at ip " + exe.getIp());
        }
        OneInstruction ix = allInstructions[opcode];
        MicroCode m = ix.microCode;
        String s = ix.longName;
        Log.info(s);
        m.execute(exe, ix);
    }
}
