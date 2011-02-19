package com.mycompany.project.client;


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

        public OneInstruction(String shortName, String longName, InstructionToString toStr) {
            this.shortName = shortName;
            this.longName = longName;
            if(toStr == byteOperand) {
                this.length = 2;
            } else {
                this.length = 1;
            }
        }
    }

    private OneInstruction[] oi = new OneInstruction[] {
            new OneInstruction("nop", "nop",  noOperand),
            new OneInstruction("lxi", "lxi b", wordOperand),
            new OneInstruction("stax", "stax b",  noOperand),
            new OneInstruction("inx", "inx b",  noOperand),
            new OneInstruction("inr", "inr b",  noOperand),
            new OneInstruction("dcr", "dcr b",  noOperand),
            new OneInstruction("mvi", "mvi b", byteOperand),
            new OneInstruction("rlc", "rlc",  noOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("dad", "dad b",  noOperand),
            new OneInstruction("ldax", "ldax b",  noOperand),
            
            new OneInstruction("dcx", "dcx b",  noOperand),
            new OneInstruction("inr", "inr c",  noOperand),
            new OneInstruction("dcr", "dcr c",  noOperand),
            new OneInstruction("mvi", "mvi c", byteOperand),
            new OneInstruction("rrc", "rrc",  noOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("lxi", "lxi d", wordOperand),
            new OneInstruction("stax", "stax d",  noOperand),
            new OneInstruction("inx", "inx d",  noOperand),
            new OneInstruction("inr", "inr d",  noOperand),
            //20
            new OneInstruction("dcr", "dcr d",  noOperand),
            new OneInstruction("mvi", "mvi d", byteOperand),
            new OneInstruction("ral", "ral",  noOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("dad", "dad d",  noOperand),
            new OneInstruction("ldax", "ldax d",  noOperand),
            new OneInstruction("dcx", "dcx d",  noOperand),
            new OneInstruction("inr", "inr e",  noOperand),
            new OneInstruction("dcr", "dcr e",  noOperand),
            new OneInstruction("mvi", "mvi e",  byteOperand),

            new OneInstruction("rar", "rar",  noOperand),
            new OneInstruction("rim", "rim",  noOperand),
            new OneInstruction("lxi", "lxi h",  wordOperand),
            new OneInstruction("shld", "shld",  wordOperand),
            new OneInstruction("inx", "inx h",  noOperand),
            new OneInstruction("inr", "inr h",  noOperand),
            new OneInstruction("dcr", "dcr h",  noOperand),
            new OneInstruction("mvi", "mvi h",  byteOperand),
            new OneInstruction("daa", "daa",  noOperand),

            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("dad", "dad h",  noOperand),
            new OneInstruction("lhld", "lhld",  wordOperand),
            new OneInstruction("dcx", "dcx h",  noOperand),
            new OneInstruction("inr", "inr l",  noOperand),
            new OneInstruction("dcr", "dcr l",  noOperand),
            new OneInstruction("mvi", "mvi l",  byteOperand),
            new OneInstruction("cma", "cma",  noOperand),
            new OneInstruction("sim", "sim",  noOperand),
            new OneInstruction("lxi", "lxi sp",  wordOperand),

            new OneInstruction("sta", "sta",  wordOperand),
            new OneInstruction("inx", "inx sp",  noOperand),
            new OneInstruction("inr", "inr m",  noOperand),
            new OneInstruction("dcr", "dcr m",  noOperand),
            new OneInstruction("mvi", "mvi m",  byteOperand),
            new OneInstruction("stc", "stc",  noOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("dad", "dad sp",  noOperand),
            new OneInstruction("lda", "lda",  wordOperand),
            new OneInstruction("dcx", "dcx sp",  noOperand),

            new OneInstruction("inr", "inr a",  noOperand),
            new OneInstruction("dcr", "dcr a",  noOperand),
            new OneInstruction("mvi", "mvi a",  byteOperand),
            new OneInstruction("cmd", "cmd",  noOperand),
            new OneInstruction("mov", "mov b,b",  noOperand),
            new OneInstruction("mov", "mov b,c",  noOperand),
            new OneInstruction("mov", "mov b,d",  noOperand),
            new OneInstruction("mov", "mov b,e",  noOperand),
            new OneInstruction("mov", "mov b,h",  noOperand),
            new OneInstruction("mov", "mov b,l",  noOperand),
            new OneInstruction("mov", "mov b,m",  noOperand),
            new OneInstruction("mov", "mov b,a",  noOperand),
            new OneInstruction("mov", "mov c,b",  noOperand),
            new OneInstruction("mov", "mov c,c",  noOperand),
            new OneInstruction("mov", "mov c,d",  noOperand),
            new OneInstruction("mov", "mov c,e",  noOperand),
            new OneInstruction("mov", "mov c,h",  noOperand),
            new OneInstruction("mov", "mov c,l",  noOperand),
            new OneInstruction("mov", "mov c,m",  noOperand),
            new OneInstruction("mov", "mov c,a",  noOperand),
            new OneInstruction("mov", "mov d,b",  noOperand),
            new OneInstruction("mov", "mov d,c",  noOperand),
            new OneInstruction("mov", "mov d,d",  noOperand),
            new OneInstruction("mov", "mov d,e",  noOperand),
            new OneInstruction("mov", "mov d,h",  noOperand),
            new OneInstruction("mov", "mov d,l",  noOperand),
            new OneInstruction("mov", "mov d,m",  noOperand),
            new OneInstruction("mov", "mov d,a",  noOperand),
            new OneInstruction("mov", "mov e,b",  noOperand),
            new OneInstruction("mov", "mov e,c",  noOperand),
            new OneInstruction("mov", "mov e,d",  noOperand),
            new OneInstruction("mov", "mov e,h",  noOperand),
            new OneInstruction("mov", "mov e,l",  noOperand),
            new OneInstruction("mov", "mov e,m",  noOperand),
            new OneInstruction("mov", "mov e,a",  noOperand),
            new OneInstruction("mov", "mov h,b",  noOperand),
            new OneInstruction("mov", "mov h,c",  noOperand),
            new OneInstruction("mov", "mov h,d",  noOperand),
            new OneInstruction("mov", "mov h,e",  noOperand),
            new OneInstruction("mov", "mov h,h",  noOperand),
            new OneInstruction("mov", "mov h,l",  noOperand),
            new OneInstruction("mov", "mov h,m",  noOperand),
            new OneInstruction("mov", "mov h,a",  noOperand),
            new OneInstruction("mov", "mov l,b",  noOperand),
            new OneInstruction("mov", "mov l,c",  noOperand),
            new OneInstruction("mov", "mov l,d",  noOperand),
            new OneInstruction("mov", "mov l,e",  noOperand),

            new OneInstruction("mov", "mov m,a",  noOperand),
            new OneInstruction("mov", "mov a,b",  noOperand),
            new OneInstruction("mov", "mov a,c",  noOperand),
            new OneInstruction("mov", "mov a,d",  noOperand),
            new OneInstruction("mov", "mov a,e",  noOperand),
            new OneInstruction("mov", "mov a,h",  noOperand),
            new OneInstruction("mov", "mov a,l",  noOperand),
            new OneInstruction("mov", "mov a,m",  noOperand),
            new OneInstruction("mov", "mov a,a",  noOperand),

            new OneInstruction("add", "add b",  noOperand),
            new OneInstruction("add", "add c",  noOperand),
            new OneInstruction("add", "add d",  noOperand),
            new OneInstruction("add", "add e",  noOperand),
            new OneInstruction("add", "add h",  noOperand),
            new OneInstruction("add", "add l",  noOperand),
            new OneInstruction("add", "add m",  noOperand),
            new OneInstruction("add", "add a",  noOperand),

            new OneInstruction("adc", "adc b",  noOperand),
            new OneInstruction("adc", "adc c",  noOperand),
            new OneInstruction("adc", "adc d",  noOperand),
            new OneInstruction("adc", "adc e",  noOperand),
            new OneInstruction("adc", "adc h",  noOperand),
            new OneInstruction("adc", "adc l",  noOperand),
            new OneInstruction("adc", "adc m",  noOperand),
            new OneInstruction("adc", "adc a",  noOperand),

            new OneInstruction("sub", "sub b",  noOperand),
            new OneInstruction("sub", "sub c",  noOperand),
            new OneInstruction("sub", "sub d",  noOperand),
            new OneInstruction("sub", "sub e",  noOperand),
            new OneInstruction("sub", "sub h",  noOperand),
            new OneInstruction("sub", "sub l",  noOperand),
            new OneInstruction("sub", "sub m",  noOperand),
            new OneInstruction("sub", "sub a",  noOperand),

            new OneInstruction("sbb", "sbb b",  noOperand),
            new OneInstruction("sbb", "sbb c",  noOperand),
            new OneInstruction("sbb", "sbb d",  noOperand),
            new OneInstruction("sbb", "sbb e",  noOperand),
            new OneInstruction("sbb", "sbb h",  noOperand),
            new OneInstruction("sbb", "sbb l",  noOperand),
            new OneInstruction("sbb", "sbb m",  noOperand),
            new OneInstruction("sbb", "sbb a",  noOperand),

            new OneInstruction("ana", "ana b",  noOperand),
            new OneInstruction("ana", "ana c",  noOperand),
            new OneInstruction("ana", "ana d",  noOperand),
            new OneInstruction("ana", "ana d",  noOperand),
            new OneInstruction("ana", "ana h",  noOperand),
            new OneInstruction("ana", "ana l",  noOperand),
            new OneInstruction("ana", "ana m",  noOperand),
            new OneInstruction("ana", "ana a",  noOperand),

            new OneInstruction("xra", "xra b",  noOperand),
            new OneInstruction("xra", "xra c",  noOperand),
            new OneInstruction("xra", "xra d",  noOperand),
            new OneInstruction("xra", "xra d",  noOperand),
            new OneInstruction("xra", "xra h",  noOperand),
            new OneInstruction("xra", "xra l",  noOperand),
            new OneInstruction("xra", "xra m",  noOperand),
            new OneInstruction("xra", "xra a",  noOperand),

            new OneInstruction("ora", "ora b",  noOperand),
            new OneInstruction("ora", "ora c",  noOperand),
            new OneInstruction("ora", "ora d",  noOperand),
            new OneInstruction("ora", "ora d",  noOperand),
            new OneInstruction("ora", "ora h",  noOperand),
            new OneInstruction("ora", "ora l",  noOperand),
            new OneInstruction("ora", "ora m",  noOperand),
            new OneInstruction("ora", "ora a",  noOperand),

            new OneInstruction("cmp", "cmp b",  noOperand),
            new OneInstruction("cmp", "cmp c",  noOperand),
            new OneInstruction("cmp", "cmp d",  noOperand),
            new OneInstruction("cmp", "cmp d",  noOperand),
            new OneInstruction("cmp", "cmp h",  noOperand),
            new OneInstruction("cmp", "cmp l",  noOperand),
            new OneInstruction("cmp", "cmp m",  noOperand),
            new OneInstruction("cmp", "cmp a",  noOperand),

            new OneInstruction("rnz", "rnz",  noOperand),
            new OneInstruction("pop", "pop b",  noOperand),
            new OneInstruction("jnz", "jnz",  wordOperand),
            new OneInstruction("jmp", "jmp",  wordOperand),
            new OneInstruction("cnz", "cnz",  wordOperand),
            new OneInstruction("push", "push b",  noOperand),
            new OneInstruction("adi", "adi",  byteOperand),
            new OneInstruction("rst", "rst 0",  noOperand),
            new OneInstruction("rz", "rz",  noOperand),
            new OneInstruction("ret", "ret",  noOperand),
            new OneInstruction("jz", "jz",  wordOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("cz", "cz",  wordOperand),
            new OneInstruction("call", "call",  wordOperand),
            new OneInstruction("aci", "aci",  byteOperand),
            new OneInstruction("rst", "rst 1",  noOperand),
            new OneInstruction("rnc", "rnc",  noOperand),
            new OneInstruction("pop", "pop d",  noOperand),
            new OneInstruction("jnc", "jnc",  noOperand),
            new OneInstruction("out", "out",  noOperand),
            new OneInstruction("cnc", "cnc",  noOperand),
            new OneInstruction("push", "push",  noOperand),
            new OneInstruction("sui", "sui",  noOperand),
            new OneInstruction("rst", "rst 2",  noOperand),
            new OneInstruction("rc", "rc",  noOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("jc", "jc",  wordOperand),
            new OneInstruction("in", "in",  byteOperand),
            new OneInstruction("cc", "cc",  wordOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("sbi", "sbi",  noOperand),
            new OneInstruction("rst", "rst 3",  noOperand),
            new OneInstruction("rpo", "rpo",  noOperand),
            new OneInstruction("pop", "pop h",  noOperand),
            new OneInstruction("jpo", "jpo",  wordOperand),
            new OneInstruction("xthl", "xthl",  noOperand),
            new OneInstruction("cpo", "cpo",  wordOperand),
            new OneInstruction("push", "push h",  noOperand),
            new OneInstruction("ani", "ani",  wordOperand),
            new OneInstruction("rst", "rst 4",  noOperand),
            new OneInstruction("rep", "rep",  noOperand),
            new OneInstruction("pchl", "pchl",  noOperand),
            new OneInstruction("jpe", "jpe",  wordOperand),
            new OneInstruction("xchg", "xchg",  wordOperand),
            new OneInstruction("cpe", "cpe",  wordOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("xri", "xri",  noOperand),
            new OneInstruction("rst", "rst 5",  noOperand),
            new OneInstruction("rp", "rp",  noOperand),
            new OneInstruction("pop", "pop psw",  noOperand),
            new OneInstruction("jp", "jp",  wordOperand),
            new OneInstruction("di", "di",  noOperand),
            new OneInstruction("cp", "cp",  wordOperand),
            new OneInstruction("push", "push psw",  noOperand),
            new OneInstruction("ori", "ori",  byteOperand),
            new OneInstruction("rst", "rst 6",  noOperand),
            new OneInstruction("rm", "rm",  noOperand),
            new OneInstruction("sphl", "sphl",  noOperand),
            new OneInstruction("jm", "jm",  wordOperand),
            new OneInstruction("ei", "ei",  noOperand),
            new OneInstruction("cm", "cm",  wordOperand),
            new OneInstruction("", "invalid",  noOperand),
            new OneInstruction("cpi", "cpi",  byteOperand),
            new OneInstruction("rst", "rst 7",  noOperand), 
    };

    public static String toString(int opcode, int op1, int op2) {
        return "";
    }
}
