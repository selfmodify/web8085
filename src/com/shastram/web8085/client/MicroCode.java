package com.shastram.web8085.client;

import java.util.logging.Logger;

import com.shastram.web8085.client.Parser.Operand;

/**
 * 
 * All instructions microcode are implemented here.
 * 
 */
public abstract class MicroCode {
    private static Logger log = Logger.getLogger(MicroCode.class.getName());

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

    private static NoOperand noOperand = new NoOperand();

    private static ByteOperand byteOperand = new ByteOperand();
    private static WordOperand wordOperand = new WordOperand();

    /**
     * Instructions arranged according to their microcode.
     */
    public static OneInstruction[] microcodeTable = new OneInstruction[] {
            new OneInstruction(0x00, "nop", "nop", noOperand, MicroCode.nop),
            new OneInstruction(0x01, "lxi", "lxi b", wordOperand, MicroCode.lxi),
            new OneInstruction(0x02, "stax", "stax b", noOperand, MicroCode.stax),
            new OneInstruction(0x03, "inx", "inx b", noOperand, MicroCode.inx),
            new OneInstruction(0x04, "inr", "inr b", noOperand, MicroCode.inr),
            new OneInstruction(0x05, "dcr", "dcr b", noOperand, MicroCode.dcr),
            new OneInstruction(0x06, "mvi", "mvi b", byteOperand, MicroCode.mvi),
            new OneInstruction(0x07, "rlc", "rlc", noOperand, MicroCode.rlc),
            new OneInstruction(0x08, "", "assert", noOperand, MicroCode.assertRunner),
            new OneInstruction(0x09, "dad", "dad b", noOperand, MicroCode.dad),
            new OneInstruction(0x0a, "ldax", "ldax b", noOperand, MicroCode.ldax),
            new OneInstruction(0x0b, "dcx", "dcx b", noOperand, MicroCode.dcx),
            new OneInstruction(0x0c, "inr", "inr c", noOperand, MicroCode.inr),
            new OneInstruction(0x0d, "dcr", "dcr c", noOperand, MicroCode.dcr),
            new OneInstruction(0x0e, "mvi", "mvi c", byteOperand, MicroCode.mvi),
            new OneInstruction(0x0f, "rrc", "rrc", noOperand, MicroCode.rrc),

            new OneInstruction(0x10, ".break", "invalid", noOperand, MicroCode.breakRunner),
            new OneInstruction(0x11, "lxi", "lxi d", wordOperand, MicroCode.lxi),
            new OneInstruction(0x12, "stax", "stax d", noOperand, MicroCode.stax),
            new OneInstruction(0x13, "inx", "inx d", noOperand, MicroCode.inx),
            new OneInstruction(0x14, "inr", "inr d", noOperand, MicroCode.inr),
            new OneInstruction(0x15, "dcr", "dcr d", noOperand, MicroCode.dcr),
            new OneInstruction(0x16, "mvi", "mvi d", byteOperand, MicroCode.mvi),
            new OneInstruction(0x17, "ral", "ral", noOperand, MicroCode.ral),
            new OneInstruction(0x18, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0x19, "dad", "dad d", noOperand, MicroCode.dad),
            new OneInstruction(0x1a, "ldax", "ldax d", noOperand, MicroCode.ldax),
            new OneInstruction(0x1b, "dcx", "dcx d", noOperand, MicroCode.dcx),
            new OneInstruction(0x1c, "inr", "inr e", noOperand, MicroCode.inr),
            new OneInstruction(0x1d, "dcr", "dcr e", noOperand, MicroCode.dcr),
            new OneInstruction(0x1e, "mvi", "mvi e", byteOperand, MicroCode.mvi),
            new OneInstruction(0x1f, "rar", "rar", noOperand, MicroCode.rar),

            new OneInstruction(0x20, "rim", "rim", noOperand, MicroCode.nop),
            new OneInstruction(0x21, "lxi", "lxi h", wordOperand, MicroCode.lxi),
            new OneInstruction(0x22, "shld", "shld", wordOperand, MicroCode.shld),
            new OneInstruction(0x23, "inx", "inx h", noOperand, MicroCode.inx),
            new OneInstruction(0x24, "inr", "inr h", noOperand, MicroCode.inr),
            new OneInstruction(0x25, "dcr", "dcr h", noOperand, MicroCode.dcr),
            new OneInstruction(0x26, "mvi", "mvi h", byteOperand, MicroCode.mvi),
            new OneInstruction(0x27, "daa", "daa", noOperand, MicroCode.nop),
            new OneInstruction(0x28, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0x29, "dad", "dad h", noOperand, MicroCode.dad),
            new OneInstruction(0x2a, "lhld", "lhld", wordOperand, MicroCode.lhld),
            new OneInstruction(0x2b, "dcx", "dcx h", noOperand, MicroCode.dcx),
            new OneInstruction(0x2c, "inr", "inr l", noOperand, MicroCode.inr),
            new OneInstruction(0x2d, "dcr", "dcr l", noOperand, MicroCode.dcr),
            new OneInstruction(0x2e, "mvi", "mvi l", byteOperand, MicroCode.mvi),
            new OneInstruction(0x2f, "cma", "cma", noOperand, MicroCode.cma),

            new OneInstruction(0x30, "sim", "sim", noOperand, MicroCode.nop),
            new OneInstruction(0x31, "lxi", "lxi sp", wordOperand, MicroCode.lxi),
            new OneInstruction(0x32, "sta", "sta", wordOperand, MicroCode.sta),
            new OneInstruction(0x33, "inx", "inx sp", noOperand, MicroCode.inx),
            new OneInstruction(0x34, "inr", "inr m", noOperand, MicroCode.inr),
            new OneInstruction(0x35, "dcr", "dcr m", noOperand, MicroCode.dcr),
            new OneInstruction(0x36, "mvi", "mvi m", byteOperand, MicroCode.mvi),
            new OneInstruction(0x37, "stc", "stc", noOperand, MicroCode.stc),
            new OneInstruction(0x38, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0x39, "dad", "dad sp", noOperand, MicroCode.dad),
            new OneInstruction(0x3a, "lda", "lda", wordOperand, MicroCode.lda),
            new OneInstruction(0x3b, "dcx", "dcx sp", noOperand, MicroCode.dcx),
            new OneInstruction(0x3c, "inr", "inr a", noOperand, MicroCode.inr),
            new OneInstruction(0x3d, "dcr", "dcr a", noOperand, MicroCode.dcr),
            new OneInstruction(0x3e, "mvi", "mvi a", byteOperand, MicroCode.mvi),
            new OneInstruction(0x3f, "cmc", "cmc", noOperand, MicroCode.cmc),

            new OneInstruction(0x40, "mov", "mov b,b", noOperand, MicroCode.move),
            new OneInstruction(0x41, "mov", "mov b,c", noOperand, MicroCode.move),
            new OneInstruction(0x42, "mov", "mov b,d", noOperand, MicroCode.move),
            new OneInstruction(0x43, "mov", "mov b,e", noOperand, MicroCode.move),
            new OneInstruction(0x44, "mov", "mov b,h", noOperand, MicroCode.move),
            new OneInstruction(0x45, "mov", "mov b,l", noOperand, MicroCode.move),
            new OneInstruction(0x46, "mov", "mov b,m", noOperand, MicroCode.move),
            new OneInstruction(0x47, "mov", "mov b,a", noOperand, MicroCode.move),
            new OneInstruction(0x48, "mov", "mov c,b", noOperand, MicroCode.move),
            new OneInstruction(0x49, "mov", "mov c,c", noOperand, MicroCode.move),
            new OneInstruction(0x4a, "mov", "mov c,d", noOperand, MicroCode.move),
            new OneInstruction(0x4b, "mov", "mov c,e", noOperand, MicroCode.move),
            new OneInstruction(0x4c, "mov", "mov c,h", noOperand, MicroCode.move),
            new OneInstruction(0x4d, "mov", "mov c,l", noOperand, MicroCode.move),
            new OneInstruction(0x4e, "mov", "mov c,m", noOperand, MicroCode.move),
            new OneInstruction(0x4f, "mov", "mov c,a", noOperand, MicroCode.move),

            new OneInstruction(0x50, "mov", "mov d,b", noOperand, MicroCode.move),
            new OneInstruction(0x51, "mov", "mov d,c", noOperand, MicroCode.move),
            new OneInstruction(0x52, "mov", "mov d,d", noOperand, MicroCode.move),
            new OneInstruction(0x53, "mov", "mov d,e", noOperand, MicroCode.move),
            new OneInstruction(0x54, "mov", "mov d,h", noOperand, MicroCode.move),
            new OneInstruction(0x55, "mov", "mov d,l", noOperand, MicroCode.move),
            new OneInstruction(0x56, "mov", "mov d,m", noOperand, MicroCode.move),
            new OneInstruction(0x57, "mov", "mov d,a", noOperand, MicroCode.move),
            new OneInstruction(0x58, "mov", "mov e,b", noOperand, MicroCode.move),
            new OneInstruction(0x59, "mov", "mov e,c", noOperand, MicroCode.move),
            new OneInstruction(0x5a, "mov", "mov e,d", noOperand, MicroCode.move),
            new OneInstruction(0x5b, "mov", "mov e,e", noOperand, MicroCode.move),
            new OneInstruction(0x5c, "mov", "mov e,h", noOperand, MicroCode.move),
            new OneInstruction(0x5d, "mov", "mov e,l", noOperand, MicroCode.move),
            new OneInstruction(0x5e, "mov", "mov e,m", noOperand, MicroCode.move),
            new OneInstruction(0x5f, "mov", "mov e,a", noOperand, MicroCode.move),

            new OneInstruction(0x60, "mov", "mov h,b", noOperand, MicroCode.move),
            new OneInstruction(0x61, "mov", "mov h,c", noOperand, MicroCode.move),
            new OneInstruction(0x62, "mov", "mov h,d", noOperand, MicroCode.move),
            new OneInstruction(0x63, "mov", "mov h,e", noOperand, MicroCode.move),
            new OneInstruction(0x64, "mov", "mov h,h", noOperand, MicroCode.move),
            new OneInstruction(0x65, "mov", "mov h,l", noOperand, MicroCode.move),
            new OneInstruction(0x66, "mov", "mov h,m", noOperand, MicroCode.move),
            new OneInstruction(0x67, "mov", "mov h,a", noOperand, MicroCode.move),
            new OneInstruction(0x68, "mov", "mov l,b", noOperand, MicroCode.move),
            new OneInstruction(0x69, "mov", "mov l,c", noOperand, MicroCode.move),
            new OneInstruction(0x6a, "mov", "mov l,d", noOperand, MicroCode.move),
            new OneInstruction(0x6b, "mov", "mov l,e", noOperand, MicroCode.move),
            new OneInstruction(0x6c, "mov", "mov l,h", noOperand, MicroCode.move),
            new OneInstruction(0x6d, "mov", "mov l,l", noOperand, MicroCode.move),
            new OneInstruction(0x6e, "mov", "mov l,m", noOperand, MicroCode.move),
            new OneInstruction(0x6f, "mov", "mov l,a", noOperand, MicroCode.move),

            new OneInstruction(0x70, "mov", "mov m,b", noOperand, MicroCode.move),
            new OneInstruction(0x71, "mov", "mov m,c", noOperand, MicroCode.move),
            new OneInstruction(0x72, "mov", "mov m,d", noOperand, MicroCode.move),
            new OneInstruction(0x73, "mov", "mov m,e", noOperand, MicroCode.move),
            new OneInstruction(0x74, "mov", "mov m,h", noOperand, MicroCode.move),
            new OneInstruction(0x75, "mov", "mov m,l", noOperand, MicroCode.move),
            new OneInstruction(0x76, "hlt", "hlt", noOperand, MicroCode.hlt),
            new OneInstruction(0x77, "mov", "mov m,a", noOperand, MicroCode.move),
            new OneInstruction(0x78, "mov", "mov a,b", noOperand, MicroCode.move),
            new OneInstruction(0x79, "mov", "mov a,c", noOperand, MicroCode.move),
            new OneInstruction(0x7a, "mov", "mov a,d", noOperand, MicroCode.move),
            new OneInstruction(0x7b, "mov", "mov a,e", noOperand, MicroCode.move),
            new OneInstruction(0x7c, "mov", "mov a,h", noOperand, MicroCode.move),
            new OneInstruction(0x7d, "mov", "mov a,l", noOperand, MicroCode.move),
            new OneInstruction(0x7e, "mov", "mov a,m", noOperand, MicroCode.move),
            new OneInstruction(0x7f, "mov", "mov a,a", noOperand, MicroCode.move),

            new OneInstruction(0x80, "add", "add b", noOperand, MicroCode.add),
            new OneInstruction(0x81, "add", "add c", noOperand, MicroCode.add),
            new OneInstruction(0x82, "add", "add d", noOperand, MicroCode.add),
            new OneInstruction(0x83, "add", "add e", noOperand, MicroCode.add),
            new OneInstruction(0x84, "add", "add h", noOperand, MicroCode.add),
            new OneInstruction(0x85, "add", "add l", noOperand, MicroCode.add),
            new OneInstruction(0x86, "add", "add m", noOperand, MicroCode.add),
            new OneInstruction(0x87, "add", "add a", noOperand, MicroCode.add),
            new OneInstruction(0x88, "adc", "adc b", noOperand, MicroCode.adc),
            new OneInstruction(0x89, "adc", "adc c", noOperand, MicroCode.adc),
            new OneInstruction(0x8a, "adc", "adc d", noOperand, MicroCode.adc),
            new OneInstruction(0x8b, "adc", "adc e", noOperand, MicroCode.adc),
            new OneInstruction(0x8c, "adc", "adc h", noOperand, MicroCode.adc),
            new OneInstruction(0x8d, "adc", "adc l", noOperand, MicroCode.adc),
            new OneInstruction(0x8e, "adc", "adc m", noOperand, MicroCode.adc),
            new OneInstruction(0x8f, "adc", "adc a", noOperand, MicroCode.adc),

            new OneInstruction(0x90, "sub", "sub b", noOperand, MicroCode.sub),
            new OneInstruction(0x91, "sub", "sub c", noOperand, MicroCode.sub),
            new OneInstruction(0x92, "sub", "sub d", noOperand, MicroCode.sub),
            new OneInstruction(0x93, "sub", "sub e", noOperand, MicroCode.sub),
            new OneInstruction(0x94, "sub", "sub h", noOperand, MicroCode.sub),
            new OneInstruction(0x95, "sub", "sub l", noOperand, MicroCode.sub),
            new OneInstruction(0x96, "sub", "sub m", noOperand, MicroCode.sub),
            new OneInstruction(0x97, "sub", "sub a", noOperand, MicroCode.sub),
            new OneInstruction(0x98, "sbb", "sbb b", noOperand, MicroCode.sbb),
            new OneInstruction(0x99, "sbb", "sbb c", noOperand, MicroCode.sbb),
            new OneInstruction(0x9a, "sbb", "sbb d", noOperand, MicroCode.sbb),
            new OneInstruction(0x9b, "sbb", "sbb e", noOperand, MicroCode.sbb),
            new OneInstruction(0x9c, "sbb", "sbb h", noOperand, MicroCode.sbb),
            new OneInstruction(0x9d, "sbb", "sbb l", noOperand, MicroCode.sbb),
            new OneInstruction(0x9e, "sbb", "sbb m", noOperand, MicroCode.sbb),
            new OneInstruction(0x9f, "sbb", "sbb a", noOperand, MicroCode.sbb),

            new OneInstruction(0xa0, "ana", "ana b", noOperand, MicroCode.ana),
            new OneInstruction(0xa1, "ana", "ana c", noOperand, MicroCode.ana),
            new OneInstruction(0xa2, "ana", "ana d", noOperand, MicroCode.ana),
            new OneInstruction(0xa3, "ana", "ana d", noOperand, MicroCode.ana),
            new OneInstruction(0xa4, "ana", "ana h", noOperand, MicroCode.ana),
            new OneInstruction(0xa5, "ana", "ana l", noOperand, MicroCode.ana),
            new OneInstruction(0xa6, "ana", "ana m", noOperand, MicroCode.ana),
            new OneInstruction(0xa7, "ana", "ana a", noOperand, MicroCode.ana),
            new OneInstruction(0xa8, "xra", "xra b", noOperand, MicroCode.xra),
            new OneInstruction(0xa9, "xra", "xra c", noOperand, MicroCode.xra),
            new OneInstruction(0xaa, "xra", "xra d", noOperand, MicroCode.xra),
            new OneInstruction(0xab, "xra", "xra d", noOperand, MicroCode.xra),
            new OneInstruction(0xac, "xra", "xra h", noOperand, MicroCode.xra),
            new OneInstruction(0xad, "xra", "xra l", noOperand, MicroCode.xra),
            new OneInstruction(0xae, "xra", "xra m", noOperand, MicroCode.xra),
            new OneInstruction(0xaf, "xra", "xra a", noOperand, MicroCode.xra),

            new OneInstruction(0xb0, "ora", "ora b", noOperand, MicroCode.ora),
            new OneInstruction(0xb1, "ora", "ora c", noOperand, MicroCode.ora),
            new OneInstruction(0xb2, "ora", "ora d", noOperand, MicroCode.ora),
            new OneInstruction(0xb3, "ora", "ora d", noOperand, MicroCode.ora),
            new OneInstruction(0xb4, "ora", "ora h", noOperand, MicroCode.ora),
            new OneInstruction(0xb5, "ora", "ora l", noOperand, MicroCode.ora),
            new OneInstruction(0xb6, "ora", "ora m", noOperand, MicroCode.ora),
            new OneInstruction(0xb7, "ora", "ora a", noOperand, MicroCode.ora),
            new OneInstruction(0xb8, "cmp", "cmp b", noOperand, MicroCode.cmp),
            new OneInstruction(0xb9, "cmp", "cmp c", noOperand, MicroCode.cmp),
            new OneInstruction(0xba, "cmp", "cmp d", noOperand, MicroCode.cmp),
            new OneInstruction(0xbb, "cmp", "cmp d", noOperand, MicroCode.cmp),
            new OneInstruction(0xbc, "cmp", "cmp h", noOperand, MicroCode.cmp),
            new OneInstruction(0xbd, "cmp", "cmp l", noOperand, MicroCode.cmp),
            new OneInstruction(0xbe, "cmp", "cmp m", noOperand, MicroCode.cmp),
            new OneInstruction(0xbf, "cmp", "cmp a", noOperand, MicroCode.cmp),

            new OneInstruction(0xc0, "rnz", "rnz", noOperand, MicroCode.nop),
            new OneInstruction(0xc1, "pop", "pop b", noOperand, MicroCode.nop),
            new OneInstruction(0xc2, "jnz", "jnz", wordOperand, MicroCode.nop),
            new OneInstruction(0xc3, "jmp", "jmp", wordOperand, MicroCode.nop),
            new OneInstruction(0xc4, "cnz", "cnz", wordOperand, MicroCode.nop),
            new OneInstruction(0xc5, "push", "push b", noOperand, MicroCode.nop),
            new OneInstruction(0xc6, "adi", "adi", byteOperand, MicroCode.adi),
            new OneInstruction(0xc7, "rst", "rst 0", noOperand, MicroCode.nop),
            new OneInstruction(0xc8, "rz", "rz", noOperand, MicroCode.nop),
            new OneInstruction(0xc9, "ret", "ret", noOperand, MicroCode.nop),
            new OneInstruction(0xca, "jz", "jz", wordOperand, MicroCode.nop),
            new OneInstruction(0xcb, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0xcc, "cz", "cz", wordOperand, MicroCode.nop),
            new OneInstruction(0xcd, "call", "call", wordOperand, MicroCode.nop),
            new OneInstruction(0xce, "aci", "aci", byteOperand, MicroCode.aci),
            new OneInstruction(0xcf, "rst", "rst 1", noOperand, MicroCode.nop),

            new OneInstruction(0xd0, "rnc", "rnc", noOperand, MicroCode.nop),
            new OneInstruction(0xd1, "pop", "pop d", noOperand, MicroCode.nop),
            new OneInstruction(0xd2, "jnc", "jnc", noOperand, MicroCode.nop),
            new OneInstruction(0xd3, "out", "out", noOperand, MicroCode.nop),
            new OneInstruction(0xd4, "cnc", "cnc", noOperand, MicroCode.nop),
            new OneInstruction(0xd5, "push", "push", noOperand, MicroCode.nop),
            new OneInstruction(0xd6, "sui", "sui", byteOperand, MicroCode.sui),
            new OneInstruction(0xd7, "rst", "rst 2", noOperand, MicroCode.nop),
            new OneInstruction(0xd8, "rc", "rc", noOperand, MicroCode.nop),
            new OneInstruction(0xd9, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0xda, "jc", "jc", wordOperand, MicroCode.nop),
            new OneInstruction(0xdb, "in", "in", byteOperand, MicroCode.nop),
            new OneInstruction(0xdc, "cc", "cc", wordOperand, MicroCode.nop),
            new OneInstruction(0xdd, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0xde, "sbi", "sbi", noOperand, MicroCode.sbi),
            new OneInstruction(0xdf, "rst", "rst 3", noOperand, MicroCode.nop),

            new OneInstruction(0xe0, "rpo", "rpo", noOperand, MicroCode.nop),
            new OneInstruction(0xe1, "pop", "pop h", noOperand, MicroCode.nop),
            new OneInstruction(0xe2, "jpo", "jpo", wordOperand, MicroCode.nop),
            new OneInstruction(0xe3, "xthl", "xthl", noOperand, MicroCode.nop),
            new OneInstruction(0xe4, "cpo", "cpo", wordOperand, MicroCode.nop),
            new OneInstruction(0xe5, "push", "push h", noOperand, MicroCode.nop),
            new OneInstruction(0xe6, "ani", "ani", wordOperand, MicroCode.ani),
            new OneInstruction(0xe7, "rst", "rst 4", noOperand, MicroCode.nop),
            new OneInstruction(0xe8, "rep", "rep", noOperand, MicroCode.nop),
            new OneInstruction(0xe9, "pchl", "pchl", noOperand, MicroCode.nop),
            new OneInstruction(0xea, "jpe", "jpe", wordOperand, MicroCode.nop),
            new OneInstruction(0xeb, "xchg", "xchg", noOperand, MicroCode.xchg),
            new OneInstruction(0xec, "cpe", "cpe", wordOperand, MicroCode.nop),
            new OneInstruction(0xed, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0xee, "xri", "xri", noOperand, MicroCode.xri),
            new OneInstruction(0xef, "rst", "rst 5", noOperand, MicroCode.nop),

            new OneInstruction(0xf0, "rp", "rp", noOperand, MicroCode.nop),
            new OneInstruction(0xf1, "pop", "pop psw", noOperand, MicroCode.nop),
            new OneInstruction(0xf2, "jp", "jp", wordOperand, MicroCode.nop),
            new OneInstruction(0xf3, "di", "di", noOperand, MicroCode.nop),
            new OneInstruction(0xf4, "cp", "cp", wordOperand, MicroCode.nop),
            new OneInstruction(0xf5, "push", "push psw", noOperand, MicroCode.nop),
            new OneInstruction(0xf6, "ori", "ori", byteOperand, MicroCode.ori),
            new OneInstruction(0xf7, "rst", "rst 6", noOperand, MicroCode.nop),
            new OneInstruction(0xf8, "rm", "rm", noOperand, MicroCode.nop),
            new OneInstruction(0xf9, "sphl", "sphl", noOperand, MicroCode.nop),
            new OneInstruction(0xfa, "jm", "jm", wordOperand, MicroCode.nop),
            new OneInstruction(0xfb, "ei", "ei", noOperand, MicroCode.nop),
            new OneInstruction(0xfc, "cm", "cm", wordOperand, MicroCode.nop),
            new OneInstruction(0xfd, "", "invalid", noOperand, MicroCode.nop),
            new OneInstruction(0xfe, "cpi", "cpi", byteOperand, MicroCode.cpi),
            new OneInstruction(0xff, "rst", "rst 7", noOperand, MicroCode.nop),
    };

    public static String toString(int opcode, int op1) {
        String str = opcode >= 0 && opcode < microcodeTable.length ? microcodeTable[opcode].toString(op1) : "";
        return str;
    }

    public static void execute(Exe exe) throws Exception {
        int opcode = exe.getMemAtIp();
        if (opcode < 0 || opcode >= microcodeTable.length) {
            throw new Exception("Invalid opcode " + opcode + " at ip " + exe.getIp());
        }
        OneInstruction ix = microcodeTable[opcode];
        MicroCode m = ix.microCode;
        if (Config.printInstructions) {
            String s = ix.longName;
            DebugLineInfo debugInfo = exe.getDebugInfo(exe.ip);
            log.info("Executing " + s + " " + (debugInfo != null ? "Line=" + debugInfo.line : ""));
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
                log.info("HLT executed. " + exe.getRegisterValues());
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

    /**
     * The contents of the accumulator are stored in the memory location
     * specified
     */
    public static MicroCode sta = new MicroCode() {
        @Override
        public void execute(Exe exe, OneInstruction i) throws Exception {
            exe.nextIp();
            int addr = exe.read16bit();
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
                assertCompare(exe, value, num, "Expected memory at address [" + addr + "] = ");
            } else {
                // try to parse it as a register. 
                op = OperandParser.parseRegisterForAssert(lhs);
                String msg = "Expected " + op.toString() + "=";
                int got = exe.getRegOrMem(op);
                assertCompare(exe, num, got, msg);
            }
        }
        if (Config.printAssertions) {
            log.info("Assertion passed at ip=" + exe.ip + " " + line);
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

    public abstract void execute(Exe exe, OneInstruction i) throws Exception;
}
