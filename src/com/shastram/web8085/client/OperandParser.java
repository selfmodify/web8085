package com.shastram.web8085.client;

import java.util.HashMap;

import com.shastram.web8085.client.InstructionParser.Operand;

public abstract class OperandParser {
    private static HashMap<String, InstructionParser.Operand> map = createOperandMap();

    /**
     * map containing the ip address to the assert instruction.
     */
    private static HashMap<Integer, String> assertOperation = new HashMap<Integer, String>();

    private static HashMap<String, Operand> createOperandMap() {
        HashMap<String, Operand> map = new HashMap<String, Operand>();
        map.put("a", InstructionParser.Operand.A);
        map.put("b", InstructionParser.Operand.B);
        map.put("c", InstructionParser.Operand.C);
        map.put("d", InstructionParser.Operand.D);
        map.put("f", InstructionParser.Operand.E);
        map.put("h", InstructionParser.Operand.H);
        map.put("l", InstructionParser.Operand.L);
        return map;
    }

    public static void parseTwoOperands(InstructionParser i,String operands) throws Exception {
        String[] parts = operands.split(",", 2);
        if(parts.length < 2) {
            throw new Exception("Expected two operands");
        }
        i.op1 = map.get(parts[0]);
        i.op2 = map.get(parts[1]);
    }

    public static void parseMovOperands(InstructionParser i,String operands) throws Exception {
        parseTwoOperands(i, operands);
        if(i.op1 == InstructionParser.Operand.M && i.op2 == InstructionParser.Operand.M) {
            throw new Exception("Mov operand cannot have both operand set to memory");
        }
        i.code = 0x40 + i.op1.ordinal() * 8 + i.op2.ordinal();
    }

    public abstract void parse(InstructionParser i,String line) throws Exception;

    public static OperandParser zeroOperand = new OperandParser() {
        @Override
        public void parse(InstructionParser i, String line) throws Exception {
            i.code = i.baseCode;
        }
    };

    public static OperandParser oneOperand = new OperandParser() {
        @Override
        public void parse(InstructionParser i, String operands) throws Exception {
            i.op1 = getOperand(operands);
            i.code = i.baseCode + i.op1.ordinal();
        }

    };

    public static OperandParser remainingLine = new OperandParser() {
        @Override
        public void parse(InstructionParser i, String line) throws Exception {
            String[] parts = line.split("[ \t]+");
            insertAssertion(i.ip, line);
        }
    };

    private static Operand getOperand(String operands) throws Exception {
        Operand operand = map.get(operands.trim());
        if(operand == null) {
            throw new Exception("Operand expected. " + operands);
        }
        return operand;
    }

    public void reset() {
        assertOperation.clear();
    }

    public void insertAssertion(int ip, String assertion) {
        assertOperation.put(ip, assertion);
    }

    public String getAssertion(int ip) {
        return assertOperation.get(ip);
    }
}
