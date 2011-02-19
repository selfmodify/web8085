package com.mycompany.project.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private static HashMap<String, Instruction> instructions = loadInstructions();
    
    private static Logger logger = Logger.getLogger(Parser.class.getName());
    
    private static HashMap<String, Instruction> loadInstructions() {
        HashMap<String, Instruction> map = new HashMap<String, Instruction>();
        map.put("add", new Instruction(Instruction.Mnemonic.ADD, 0x80, OperandParser.oneOperand));
        map.put("mov", new Instruction(Instruction.Mnemonic.MOV, 0x40,  new OperandParser() {
            @Override
            public void parse(Instruction i,String operands) throws Exception {
                parseMovOperands(i, operands);
            }
        }));
        map.put("sub", new Instruction(Instruction.Mnemonic.SUB, 0x90, OperandParser.oneOperand));
        return map;
    }

    public Parser( ){
        
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
        Instruction ix = instructions.get(parts[0]);
        if(ix == null) {
            return new ParseToken(Type.SYNTAX_ERROR, line);
        }
        ix.parseOperands(parts[1]);
        ParseToken t = new ParseToken(ix, line);
        return t;
    }
    
    public static void test( ) {
        String sourceCode = "mov a,b\n sub b\nadd c";
        String[] lines = sourceCode.split("\n");
        Parser p = new Parser();
        try {
            for(String l: lines) {
                p.parseLine(lines[0]);
            }
        } catch(Exception ex) {
            logger.log(Level.WARNING,"Error parsing",ex);
        }
    }
}
