package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Logger;

public class Parser {

    private static HashMap<String, InstructionParser> instructions = loadInstructions();

    private static Logger logger = Logger.getLogger(Parser.class.getName());

    private final HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();

    private String[] source;
    private int lineNumber;

    /**
     * Parsers for the instructions
     * 
     * @return
     */
    private static HashMap<String, InstructionParser> loadInstructions() {
        HashMap<String, InstructionParser> map = new HashMap<String, InstructionParser>();
        map.put("aci", new InstructionParser(InstructionParser.Mnemonic.ACI, 0xCE, OperandParser.immediateByteOperand));
        map.put("add", new InstructionParser(InstructionParser.Mnemonic.ADD, 0x80, OperandParser.oneOperand));
        map.put("lda", new InstructionParser(InstructionParser.Mnemonic.LDA, 0x3A, OperandParser.immediateOperand));
        map.put("sta", new InstructionParser(InstructionParser.Mnemonic.STA, 0x32, OperandParser.immediateOperand));
        map.put("stax", new InstructionParser(InstructionParser.Mnemonic.STAX, 0x02, OperandParser.ldaxOrStaxOperand));
        map.put("ldax", new InstructionParser(InstructionParser.Mnemonic.LDAX, 0x0A, OperandParser.ldaxOrStaxOperand));
        map.put("stax", new InstructionParser(InstructionParser.Mnemonic.STAX, 0x02, OperandParser.ldaxOrStaxOperand));
        map.put("lhld", new InstructionParser(InstructionParser.Mnemonic.LHLD, 0x2A, OperandParser.immediateOperand));
        map.put("shld", new InstructionParser(InstructionParser.Mnemonic.SHLD, 0xDE, OperandParser.immediateOperand));
        map.put("lxi", new InstructionParser(InstructionParser.Mnemonic.LXI, 0x01, OperandParser.lxiOperand));
        map.put("xchg", new InstructionParser(InstructionParser.Mnemonic.XCHG, 0x01, OperandParser.noOperand));
        map.put(".assert", new InstructionParser(InstructionParser.Mnemonic.ASSERT, 0x8, OperandParser.remainingLine));
        map.put("stc", new InstructionParser(InstructionParser.Mnemonic.STC, 0x37, OperandParser.noOperand));
        map.put("mov", new InstructionParser(InstructionParser.Mnemonic.MOV, 0x40, new OperandParser() {
            @Override
            public void parse(Parser parser, InstructionParser i, String operands) throws Exception {
                parseMovOperands(i, operands);
            }
        }));
        map.put("mvi", new InstructionParser(InstructionParser.Mnemonic.MVI, 0x0, new OperandParser() {
            @Override
            public void parse(Parser parser, InstructionParser i, String operands) throws Exception {
                parseMviOperands(i, operands);
            }
        }));
        map.put("sub", new InstructionParser(InstructionParser.Mnemonic.SUB, 0x90, OperandParser.oneOperand));
        return map;
    }

    public Parser(String text) {
        this.source = text.split("\n");
    }

    public String nextLine() throws Exception {
        if (lineNumber > source.length) {
            throw new Exception("Reached end of source code");
        }
        return source[lineNumber++];
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
        InstructionParser ix = instructions.get(parts[0]);
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
}
