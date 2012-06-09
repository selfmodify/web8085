package com.shastram.web8085.client;

import java.util.HashMap;

import com.shastram.web8085.client.Parser.PerInstructionToken;
import com.shastram.web8085.client.Parser.Operand;

public abstract class OperandParser {
    private static HashMap<String, Operand> map = createOperandMap();

    public abstract void parse(Parser parser, PerInstructionToken i, String line) throws Exception;

    private static HashMap<String, Operand> createOperandMap() {
        HashMap<String, Operand> map = new HashMap<String, Operand>();
        map.put("a", Parser.Operand.A);
        map.put("b", Parser.Operand.B);
        map.put("c", Parser.Operand.C);
        map.put("d", Parser.Operand.D);
        map.put("e", Parser.Operand.E);
        map.put("h", Parser.Operand.H);
        map.put("l", Parser.Operand.L);
        map.put("m", Parser.Operand.M);
        return map;
    }

    /**
     * Registers must be in the range B..A
     * 
     * @param i
     * @param operands
     * @throws Exception
     */
    public static void parse2Register(PerInstructionToken i, String operands) throws Exception {
        String[] parts = getTwoOperands(operands);
        i.op1 = parseNormalRegister(parts[0]);
        i.op2 = parseNormalRegister(parts[1]);
    }

    public static String[] getTwoOperands(String operands) throws Exception {
        String[] parts = operands.split(",", 2);
        if (parts.length < 2) {
            throw new ParserException("Expected two operands");
        }
        return parts;
    }

    public static void parseRegAndByteImmediate(PerInstructionToken i, String operands) throws Exception {
        String[] parts = getTwoOperands(operands);
        i.op1 = parseNormalRegister(parts[0]);

        int num = 0;
        try {
            num = parseNumberAsByte(parts[1]);
            i.setImmediate8Bit(num);
        } catch (NumberFormatException e) {
            throw new ParserException("Not a valid number", e);
        }
    }

    /**
     * parse the string as one of the following register B,C,D,E,H,L,M,A
     * 
     * @param reg
     * @return
     * @throws Exception
     */
    public static Operand parseNormalRegister(String reg) throws Exception {
        reg = reg.trim();
        Operand op = map.get(reg);
        if (op == null) {
            throw new ParserException(reg + " is not a valid register");
        }
        if (op.ordinal() >= Parser.Operand.B.ordinal() &&
                op.ordinal() <= Parser.Operand.A.ordinal()) {
            return op;
        }
        throw new ParserException(reg + " is not a valid register");
    }

    public static Operand parseRegisterForAssert(String reg) throws Exception {
        reg = reg.trim();
        Operand op = parseRegisterPairInternal(reg);
        if (op == null) {
            op = parseNormalRegister(reg);
        }
        if (op == null) {
            throw new ParserException("Invalid register " + reg);
        }
        return op;
    }

    /**
     * internal function for parsing register pairs
     * 
     * @param reg
     * @return
     * @throws Exception
     */
    private static Operand parseRegisterPairInternal(String reg) throws Exception {
        reg = reg.trim();
        if (reg.equalsIgnoreCase("sp")) {
            return Operand.SP;
        }
        Operand op = parseNormalRegister(reg);
        if (op == Operand.B || op == Operand.D || op == Operand.H) {
            return op;
        }
        return null;
    }

    /**
     * Only one of the register pairs BC,DE,HL,SP
     * 
     * @param reg
     * @return
     * @throws Exception
     */
    public static Operand parseRegisterPair(String reg) throws Exception {
        Operand op = parseRegisterPairInternal(reg);
        if (op != null) {
            return op;
        }
        throw new Exception(reg + " is not a register pair");
    }

    public static void parseMovOperands(PerInstructionToken i, String operands) throws Exception {
        parse2Register(i, operands);
        if (i.op1 == Parser.Operand.M && i.op2 == Parser.Operand.M) {
            throw new ParserException("Mov operand cannot have both operand set to memory");
        }
        int value = 0x40 + i.op1.ordinal() * 8 + i.op2.ordinal();
        i.code = value;
    }

    protected static void parseMviOperands(PerInstructionToken i, String operands) throws Exception {
        parseRegAndByteImmediate(i, operands);
        i.code = i.op1.ordinal() * 8 + 6;
    }

    public static OperandParser noOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line) throws Exception {
            i.code = i.baseCode;
        }
    };

    public static OperandParser oneOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
            i.op1 = parseNormalRegister(operands);
            i.code = i.baseCode + i.op1.ordinal();
        }
    };

    /**
     * instructions op codes are separated by a value of 8 e.g. dcr b is 0x5 and
     * dcr c is 0x0d and so on.
     */
    public static OperandParser oneOperandSpacedBy8 = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
            i.op1 = parseNormalRegister(operands);
            i.code = i.baseCode + i.op1.ordinal() * 8;
        }
    };

    public static OperandParser inxOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
            Operand op = parseRegisterPairInternal(operands);
            if (op == Operand.SP) {
                i.code = 0x33;
            } else {
                i.code = i.baseCode + op.ordinal() * 8;
            }
        }
    };

    public static OperandParser dadOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
            Operand op = parseRegisterPairInternal(operands);
            if (op == Operand.SP) {
                i.code = 0x39;
            } else {
                i.code = i.baseCode + op.ordinal() * 8;
            }
        }
    };

    public static OperandParser dcxOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String operands) throws Exception {
            Operand op = parseRegisterPairInternal(operands);
            if (op == Operand.SP) {
                i.code = 0x3B;
            } else {
                i.code = i.baseCode + op.ordinal() * 8;
            }
        }
    };

    /**
     * Parse an immediate value operand. The value must be within 16 bits.
     */
    public static OperandParser immediateOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line)
                throws Exception {
            i.setImmediate16Bit(parseNumber(line));
            i.code = i.baseCode;
        }
    };

    /**
     * one of b, d, h, sp are allowed as the first operand
     */
    public static OperandParser lxiOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line) throws Exception {
            String[] parts = line.split(",");
            if (parts.length != 2) {
                throw new Exception("Incorrect number of operands to LXI.  Expected 2 got " + parts.length);
            }
            i.op1 = parseRegisterPair(parts[0].trim());
            i.setImmediate16Bit(parseNumber(parts[1]));
            if (i.op1 == Operand.SP) {
                i.code = 0x31;
            } else {
                i.code = i.baseCode + i.op1.ordinal() * 8;
            }
        }
    };

    public static OperandParser immediateByteOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line)
                throws Exception {
            i.setImmediate8Bit(parseNumberAsByte(line));
            i.code = i.baseCode;
        }
    };

    public static OperandParser remainingLine = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line) throws Exception {
            parser.insertAssertion(i.ip, line);
            i.code = i.baseCode;
        }
    };

    public static OperandParser breakOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line) throws Exception {
            remainingLine.parse(parser, i, line);
        }
    };

    /**
     * for ldax, stax
     */
    public static OperandParser ldaxOrStaxOperand = new OperandParser() {
        @Override
        public void parse(Parser parser, PerInstructionToken i, String line)
                throws Exception {
            Operand op = getOperand(line);
            if (op != Operand.B && op != Operand.D) {
                throw new ParserException(line + " is not a valid register. Expected B or D registers");
            }
            i.code = i.baseCode + ((op == Operand.D) ? 0x0 : 0x10);
        }
    };

    private static Operand getOperand(String operands) throws Exception {
        Operand operand = map.get(operands.trim());
        if (operand == null) {
            throw new ParserException("Operand expected. " + operands);
        }
        return operand;
    }

    /**
     * parse a string as a number. If the number ends with a H or an h then it
     * is considered a hex number
     * 
     * @param str
     * @return
     * @throws Exception
     * @throws NumberFormatException
     *             if it cannot be parsed.
     */
    public static int parseNumber(String str) throws Exception {
        int num = 0;
        str = str.trim();
        int base = 10;
        if (str.endsWith("h") || str.endsWith("H")) {
            base = 16;
            str = str.substring(0, str.length() - 1);
        }
        num = Integer.parseInt(str, base);
        if (num < 0 || num > 65535) {
            throw new ParserException("Immediate number must be in the range 0-65535 " + str);
        }
        return num;
    }

    public static int parseNumberAsByte(String line) throws Exception {
        int num = parseNumber(line);
        if (num < 0 || num > 255) {
            throw new ParserException("Immediate number must be in the range 0-255 " + line);
        }
        return (num & 0xff);
    }
}
