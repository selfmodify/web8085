package com.shastram.web8085.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private static HashMap<String, InstructionParser> instructions = loadInstructions();

    private static Logger logger = Logger.getLogger(Parser.class.getName());

    private String[] source;
    private int line;
    private int ip;

    private static HashMap<String, InstructionParser> loadInstructions() {
        HashMap<String, InstructionParser> map = new HashMap<String, InstructionParser>();
        map.put("aci", new InstructionParser(InstructionParser.Mnemonic.ACI, 0xC3, OperandParser.oneOperand));
        map.put("add", new InstructionParser(InstructionParser.Mnemonic.ADD, 0x80, OperandParser.oneOperand));
        map.put(".assert",new InstructionParser(InstructionParser.Mnemonic.ASSERT, 0x8, OperandParser.remainingLine));
        map.put("mov", new InstructionParser(InstructionParser.Mnemonic.MOV, 0x40,  new OperandParser() {
            @Override
            public void parse(InstructionParser i,String operands) throws Exception {
                parseMovOperands(i, operands);
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

    public ParseToken parseLine(String line) throws Exception {
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
        ix.parseOperands(parts.length > 1 ? parts[1] : null);
        ParseToken t = new ParseToken(ix, line);
        return t;
    }

    public static void test( ) {
        String sourceCode = "mov a,b\n sub b\nadd c";
        Parser p = new Parser(sourceCode);
        try {
            while(p.hasNext()) {
                p.parseNextLine();
            }
        } catch(Exception ex) {
            logger.log(Level.WARNING,"Error parsing",ex);
        }
    }

    public ParseToken parseNextLine() throws Exception {
        String l = nextLine();
        ParseToken token = parseLine(l);
        ip++;
        return token;
    }

    public boolean hasNext() {
        return line < source.length;
    }

}
