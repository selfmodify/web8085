package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Logger;

public class Parser {

    private static HashMap<String, InstructionParser> instructions = loadInstructions();

    private static Logger logger = Logger.getLogger(Parser.class.getName());

    private static HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();

    private String[] source;
    private int line;

    private static HashMap<String, InstructionParser> loadInstructions() {
        HashMap<String, InstructionParser> map = new HashMap<String, InstructionParser>();
        map.put("aci", new InstructionParser(InstructionParser.Mnemonic.ACI, 0xC3, OperandParser.immediateByteOperand));
        map.put("add", new InstructionParser(InstructionParser.Mnemonic.ADD, 0x80, OperandParser.oneOperand));
        map.put("lda", new InstructionParser(InstructionParser.Mnemonic.LDA, 0x3A, OperandParser.immediateOperand));
        map.put("sta", new InstructionParser(InstructionParser.Mnemonic.STA, 0x32, OperandParser.immediateOperand));
        map.put("stax", new InstructionParser(InstructionParser.Mnemonic.STAX, 0x02, OperandParser.ldaxOrStaxOperand));
        map.put("ldax", new InstructionParser(InstructionParser.Mnemonic.LDAX, 0x0A, OperandParser.ldaxOrStaxOperand));
        map.put(".assert",new InstructionParser(InstructionParser.Mnemonic.ASSERT, 0x8, OperandParser.remainingLine));
        map.put("mov", new InstructionParser(InstructionParser.Mnemonic.MOV, 0x40,  new OperandParser() {
            @Override
            public void parse(Parser parser, InstructionParser i,String operands) throws Exception {
                parseMovOperands(i, operands);
            }
        }));
        map.put("mvi", new InstructionParser(InstructionParser.Mnemonic.MVI, 0x0,  new OperandParser() {
            @Override
            public void parse(Parser parser, InstructionParser i,String operands) throws Exception {
                parseMviOperands(i, operands);
            }
        }));
        map.put("sub", new InstructionParser(InstructionParser.Mnemonic.SUB, 0x90, OperandParser.oneOperand));
        return map;
    }

    public Parser(String text ){
        this.source = text.split("\n");
    }

    public String nextLine() throws Exception {
        if(line > source.length) {
            throw new Exception("Reached end of source code");
        }
        return source[line++];
    }

    public void reset() {
        line = 0;
        source = new String[]{};
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

    public ParseToken parseLine(String line, int ip) throws Exception {
        line = line.trim().toLowerCase();
        int commentStart = line.indexOf('#');
        if( commentStart == 0 || line.length() == 0) {
            return new ParseToken(Type.COMMENT, line);
        }
        if(commentStart > 1) {
            line = line.substring(0, commentStart);
        }
        String[] parts = line.split("[\t ]",2);
        InstructionParser ix = instructions.get(parts[0]);
        if(ix == null) {
            return new ParseToken(Type.SYNTAX_ERROR, line);
        }
        ix.parseOperands(this, parts.length > 1 ? parts[1] : null, ip);
        ParseToken t = new ParseToken(ix, line);
        return t;
    }

    public ParseToken parseNextLine(int ip) throws Exception {
        String l = nextLine();
        ParseToken token = parseLine(l, ip);
        return token;
    }

    public boolean hasNext() {
        return line < source.length;
    }
}
