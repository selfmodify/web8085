package com.shastram.web8085.client;

import java.util.HashMap;

import com.shastram.web8085.client.InstructionParser.Operand;

public abstract class OperandParser {
    private static HashMap<String, InstructionParser.Operand> map = createOperandMap();

    public abstract void parse(Parser parser, InstructionParser i,String line) throws Exception;

    private static HashMap<String, Operand> createOperandMap() {
        HashMap<String, Operand> map = new HashMap<String, Operand>();
        map.put("a", InstructionParser.Operand.A);
        map.put("b", InstructionParser.Operand.B);
        map.put("c", InstructionParser.Operand.C);
        map.put("d", InstructionParser.Operand.D);
        map.put("e", InstructionParser.Operand.E);
        map.put("h", InstructionParser.Operand.H);
        map.put("l", InstructionParser.Operand.L);
        map.put("m", InstructionParser.Operand.M);
        return map;
    }

    /**
     * Registers must be in the range B..A
     * @param i
     * @param operands
     * @throws Exception
     */
    public static void parse2Register(InstructionParser i,String operands) throws Exception {
        String[] parts = getTwoOperands(operands);
        i.op1 = parseNormalRegister(parts[0]);
        i.op2 = parseNormalRegister(parts[1]);
    }

    public static String[] getTwoOperands(String operands) throws Exception {
        String[] parts = operands.split(",", 2);
        if(parts.length < 2) {
            throw new Exception("Expected two operands");
        }
        return parts;
    }

    public static void parseRegAndByteImmediate(InstructionParser i, String operands) throws Exception {
        String[] parts = getTwoOperands(operands);
        i.op1 = parseNormalRegister(parts[0]);

        int num = 0;
        try {
            num = parseNumberAsByte(parts[1]);
            i.setImmediateByte(num);
        } catch(NumberFormatException e) {
            throw new Exception("Not a valid number", e);
        }
    }

    /**
     * parse the string as one of the following register
     * B,C,D,E,H,L,M,A
     * @param reg
     * @return
     * @throws Exception
     */
    public static Operand parseNormalRegister(String reg) throws Exception {
        Operand op = map.get(reg);
        if(op == null) {
            throw new Exception(reg + " is not a valid register");
        }
        if(op.ordinal() >= InstructionParser.Operand.B.ordinal() &&
                op.ordinal() <= InstructionParser.Operand.A.ordinal()) {
            return op;
        }
        throw new Exception(reg + " is not a valid register");
    }

    public static void parseMovOperands(InstructionParser i,String operands) throws Exception {
        parse2Register(i, operands);
        if(i.op1 == InstructionParser.Operand.M && i.op2 == InstructionParser.Operand.M) {
            throw new Exception("Mov operand cannot have both operand set to memory");
        }
        int value =  0x40 + i.op1.ordinal() * 8 + i.op2.ordinal();
        i.code = value;
    }

    protected static void parseMviOperands(InstructionParser i, String operands) throws Exception {
        parseRegAndByteImmediate(i, operands);
        i.code = i.op1.ordinal() * 8 + 6;
    }

    public static OperandParser zeroOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String line) throws Exception {
            i.code = i.baseCode;
        }
    };

    public static OperandParser oneOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String operands) throws Exception {
            i.op1 = parseNormalRegister(operands);
            i.code = i.baseCode + i.op1.ordinal();
        }
    };

    /**
     * Parse an immediate value operand.  The value must be within 16 bits.
     */
    public static OperandParser immediateOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String line)
                throws Exception {
            i.setImmediate(parseNumber(line));
            i.code = i.baseCode;
        }
    };

    public static OperandParser immediateByteOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String line)
                throws Exception {
            i.setImmediate(parseNumberAsByte(line));
            i.code = i.baseCode;
        }
    };
    
    public static OperandParser remainingLine = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String line) throws Exception {
            parser.insertAssertion(i.ip, line);
            i.code = i.baseCode;
        }
    };

    /**
     * for ldax, stax
     */
    public static OperandParser ldaxOrStaxOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, InstructionParser i, String line)
                throws Exception {
            Operand op = getOperand(line);
            if(op == Operand.B || op == Operand.D) {
                throw new Exception(line + " is not a valid register. Expected B or D registers");
            }
            i.code = i.baseCode + ((op == Operand.D) ? 0x0 : 0x10);
        }
    };


    private static Operand getOperand(String operands) throws Exception {
        Operand operand = map.get(operands.trim());
        if(operand == null) {
            throw new Exception("Operand expected. " + operands);
        }
        return operand;
    }

    /**
     * parse a string as a number. If the number ends with a H or an h
     * then it is considered a hex number
     * @param str
     * @return
     * @throws Exception 
     * @throws NumberFormatException if it cannot be parsed.
     */
    public static int parseNumber(String str) throws Exception {
        int num = 0;
        str = str.trim();
        int base = 10;
        if(str.endsWith("h") || str.endsWith("H")) {
            base = 16;
            str = str.substring(0, str.length()-1);
        }
        num = Integer.parseInt(str, base);
        if(num < 0 || num > 65535) {
            throw new Exception("Immediate number must be in the range 0-65535 " + str);
        }
        return num;
    }
    public static byte parseNumberAsByte(String line) throws Exception {
        int num = parseNumber(line);
        if(num < 0 || num > 255) {
            throw new Exception("Immediate number must be in the range 0-255 " + line);
        }
        return (byte)(num & 0xff);
    }
}
