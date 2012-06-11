package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Logger;

public class Parser {

    public enum Mnemonic {
        ANA,
        ACI,
        ADC,
        ADI,
        ANI,
        CMA,
        CMC,
        CMP,
        CPI,
        DAD,
        DCR,
        DCX,
        INR,
        INX,
        MOV,
        NONE,
        ORA,
        ORI,
        ADD,
        SUB,
        SUI,
        SBB,
        SBI,
        ASSERT,
        BREAK,
        MVI,
        LDA,
        LDAX,
        LHLD,
        RAL,
        RAR,
        RRC,
        RLC,
        SHLD,
        STA,
        STAX,
        LXI,
        XCHG,
        STC,
        XRA,
        XRI,
    }

    public enum Operand {
        B, C, D, E, H, L, M, A, PSW, SP
    }

    /**
     * Per instruction tokenizer
     * 
     * @author vijay
     * 
     */
    public static class PerInstructionToken {
        public Operand op1 = null;
        public Operand op2 = null;
        public Mnemonic mnemonic = Mnemonic.NONE;
        public int baseCode = -1;
        public int code = -1;
        public String name;
        private final OperandParser oparser;
        public int ip;
        private int immediate;
        private boolean hasImmediate;
        int len = 1;

        public PerInstructionToken(Mnemonic type, int code, OperandParser oparser) {
            this.mnemonic = type;
            this.name = type.name().toLowerCase();
            this.baseCode = code;
            this.oparser = oparser;
        }

        public Mnemonic getMnemonic() {
            return mnemonic;
        }

        public void parseOperands(Parser parser, String operands, int ip) throws Exception {
            this.ip = ip;
            oparser.parse(parser, this, operands);
        }

        public void setImmediate16Bit(int num) {
            hasImmediate = true;
            immediate = num & 0xffff;
            len = 3;
        }

        public int getImmediate() throws ParserException {
            if (!hasImmediate) {
                throw new ParserException("Instruction does not have an immediate operand.");
            }
            return immediate;
        }

        public boolean hasImmediate() {
            return hasImmediate;
        }

        public void setImmediate8Bit(int num) {
            hasImmediate = true;
            immediate = num & 0xff;
            len = 2;
        }
    }

    private static HashMap<String, PerInstructionToken> instructions = loadInstructions();

    private static Logger logger = Logger.getLogger(Parser.class.getName());

    private final HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();

    private String[] source;
    private int lineNumber;

    private String currentLine;

    /**
     * Parsers for the instructions
     * 
     * @return
     */
    private static HashMap<String, PerInstructionToken> loadInstructions() {
        HashMap<String, PerInstructionToken> map = new HashMap<String, PerInstructionToken>();

        map.put("ana",
                new PerInstructionToken(Parser.Mnemonic.ANA, 0xA0, OperandParser.oneOperand));
        map.put("aci",
                new PerInstructionToken(Parser.Mnemonic.ACI, 0xCE, OperandParser.immediateByteOperand));
        map.put("adc",
                new PerInstructionToken(Parser.Mnemonic.ADC, 0x88, OperandParser.oneOperand));
        map.put("adi",
                new PerInstructionToken(Parser.Mnemonic.ADI, 0xC6, OperandParser.immediateByteOperand));
        map.put("add",
                new PerInstructionToken(Parser.Mnemonic.ADD, 0x80, OperandParser.oneOperand));
        map.put("ani",
                new PerInstructionToken(Parser.Mnemonic.ANI, 0xE6, OperandParser.immediateByteOperand));
        map.put("cma",
                new PerInstructionToken(Parser.Mnemonic.CMA, 0x2F, OperandParser.noOperand));
        map.put("cmc",
                new PerInstructionToken(Parser.Mnemonic.CMC, 0x3F, OperandParser.noOperand));
        map.put("cmp",
                new PerInstructionToken(Parser.Mnemonic.CMP, 0xB8, OperandParser.oneOperand));
        map.put("cpi",
                new PerInstructionToken(Parser.Mnemonic.CPI, 0xFE, OperandParser.immediateByteOperand));
        map.put("dad",
                new PerInstructionToken(Parser.Mnemonic.DAD, 0x09, OperandParser.dadOperand));
        map.put("dcr",
                new PerInstructionToken(Parser.Mnemonic.DCR, 0x05, OperandParser.oneOperandSpacedBy8));
        map.put("dcx",
                new PerInstructionToken(Parser.Mnemonic.DCX, 0x0B, OperandParser.dcxOperand));
        map.put("inr",
                new PerInstructionToken(Parser.Mnemonic.INR, 0x04, OperandParser.oneOperandSpacedBy8));
        map.put("inx",
                new PerInstructionToken(Parser.Mnemonic.INX, 0x03, OperandParser.inxOperand));
        map.put("ora",
                new PerInstructionToken(Parser.Mnemonic.ORA, 0xB0, OperandParser.oneOperand));
        map.put("ori",
                new PerInstructionToken(Parser.Mnemonic.ORI, 0xF6, OperandParser.immediateByteOperand));
        map.put("sub",
                new PerInstructionToken(Parser.Mnemonic.SUB, 0x91, OperandParser.oneOperand));
        map.put("sui",
                new PerInstructionToken(Parser.Mnemonic.SUI, 0xD6, OperandParser.immediateByteOperand));
        map.put("sbb",
                new PerInstructionToken(Parser.Mnemonic.SBB, 0x98, OperandParser.oneOperand));
        map.put("sbi",
                new PerInstructionToken(Parser.Mnemonic.SBI, 0xDE, OperandParser.immediateByteOperand));
        map.put("lda",
                new PerInstructionToken(Parser.Mnemonic.LDA, 0x3A, OperandParser.immediateOperand));
        map.put("sta",
                new PerInstructionToken(Parser.Mnemonic.STA, 0x32, OperandParser.immediateOperand));
        map.put("stax",
                new PerInstructionToken(Parser.Mnemonic.STAX, 0x02, OperandParser.ldaxOrStaxOperand));
        map.put("ldax",
                new PerInstructionToken(Parser.Mnemonic.LDAX, 0x0A, OperandParser.ldaxOrStaxOperand));
        map.put("stax",
                new PerInstructionToken(Parser.Mnemonic.STAX, 0x02, OperandParser.ldaxOrStaxOperand));
        map.put("lhld",
                new PerInstructionToken(Parser.Mnemonic.LHLD, 0x2A, OperandParser.immediateOperand));
        map.put("shld",
                new PerInstructionToken(Parser.Mnemonic.SHLD, 0xDE, OperandParser.immediateOperand));
        map.put("lxi",
                new PerInstructionToken(Parser.Mnemonic.LXI, 0x01, OperandParser.lxiOperand));
        map.put("xchg",
                new PerInstructionToken(Parser.Mnemonic.XCHG, 0x01, OperandParser.noOperand));
        map.put(".assert",
                new PerInstructionToken(Parser.Mnemonic.ASSERT, 0x8, OperandParser.remainingLine));
        map.put(".break",
                new PerInstructionToken(Parser.Mnemonic.BREAK, 0x10, OperandParser.breakOperand));
        map.put("stc",
                new PerInstructionToken(Parser.Mnemonic.STC, 0x37, OperandParser.noOperand));
        map.put("mov",
                new PerInstructionToken(Parser.Mnemonic.MOV, 0x40,
                        new OperandParser() {
                            @Override
                            public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
                                parseMovOperands(i, operands);
                            }
                        }));
        map.put("mvi",
                new PerInstructionToken(Parser.Mnemonic.MVI, 0x0,
                        new OperandParser() {
                            @Override
                            public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
                                parseMviOperands(i, operands);
                            }
                        }));
        map.put("ral",
                new PerInstructionToken(Parser.Mnemonic.RAL, 0x17, OperandParser.noOperand));
        map.put("rar",
                new PerInstructionToken(Parser.Mnemonic.RAR, 0x1F, OperandParser.noOperand));
        map.put("rlc",
                new PerInstructionToken(Parser.Mnemonic.RLC, 0x07, OperandParser.noOperand));
        map.put("rrc",
                new PerInstructionToken(Parser.Mnemonic.RRC, 0x0F, OperandParser.noOperand));
        map.put("sub",
                new PerInstructionToken(Parser.Mnemonic.SUB, 0x90, OperandParser.oneOperand));
        map.put("xra",
                new PerInstructionToken(Parser.Mnemonic.XRA, 0xA8, OperandParser.oneOperand));
        map.put("xri",
                new PerInstructionToken(Parser.Mnemonic.XRI, 0xEE, OperandParser.immediateByteOperand));

        return map;
    }

    public Parser(String text) {
        this.source = text.split("\n");
    }

    public String nextLine() throws Exception {
        if (lineNumber > source.length) {
            throw new Exception("Reached end of source code");
        }
        currentLine = source[lineNumber++];
        return currentLine;
    }

    public void reset() {
        lineNumber = 0;
        source = new String[] {};
        assertOperation.clear();
    }

    public void insertAssertion(int ip, String assertion) {
        assertOperation.put(ip, assertion);
    }

    public String getAssertion(int ip) {
        return assertOperation.get(ip);
    }

    public HashMap<Integer, String> getAssertionMap() {
        return assertOperation;
    }

    public ParseToken parseLine(String line, int ip, int startColumn) throws Exception {
        line = line.trim().toLowerCase();
        int commentStart = line.indexOf('#');
        int len = line.length();
        if (commentStart == 0 || len == 0) {
            return new ParseToken(Type.COMMENT, line);
        }
        if (commentStart > 1) {
            line = line.substring(0, commentStart);
        }
        String[] parts = line.split("[\t ]", 2);
        PerInstructionToken ix = instructions.get(parts[0]);
        if (ix == null) {
            Character ch = line.charAt(0);
            if (ch == '\u00a0') {
                // workaround for some strange character showing up when compiling
                // from web ui.
                return new ParseToken(Type.COMMENT, line);
            }
            return new ParseToken(Type.SYNTAX_ERROR, line);
        }
        ix.parseOperands(this, parts.length > 1 ? parts[1] : null, ip);
        int endColumn = startColumn + line.length();
        ParseToken t = new ParseToken(ix, line, lineNumber, startColumn, endColumn);
        return t;
    }

    public boolean hasNext() {
        return lineNumber < source.length;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String currentLine() {
        return currentLine;
    }
}
