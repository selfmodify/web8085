package com.mycompany.project.client;

import java.util.HashMap;

import com.mycompany.project.client.Instruction.Operand;

public abstract class OperandParser {
    private static HashMap<String, Instruction.Operand> map = createOperandMap();
    
    
    private static HashMap<String, Operand> createOperandMap() {
        HashMap<String, Operand> map = new HashMap<String, Operand>();
        map.put("a", Instruction.Operand.A);
        map.put("b", Instruction.Operand.B);
        map.put("c", Instruction.Operand.C);
        map.put("d", Instruction.Operand.D);
        map.put("f", Instruction.Operand.E);
        map.put("h", Instruction.Operand.H);
        map.put("l", Instruction.Operand.L);
        return map;
    }
    
    public static void parseTwoOperands(Instruction i,String operands) throws Exception {
        String[] parts = operands.split(",", 2);
        if(parts.length < 2) {
            throw new Exception("Expected two operands");
        }
        i.op1 = map.get(parts[0]);
        i.op2 = map.get(parts[1]);
    }
    
    public static void parseMovOperands(Instruction i,String operands) throws Exception {
        parseTwoOperands(i, operands);
        if(i.op1 == Instruction.Operand.M && i.op2 == Instruction.Operand.M) {
            throw new Exception("Mov operand cannot have both operand set to memory");
        }
        i.code = 0x40 + i.op2.ordinal() * 8 + i.op1.ordinal();
    }
    
    public abstract void parse(Instruction i,String line) throws Exception;
    
    public static OperandParser oneOperand = new OperandParser() {
        @Override
        public void parse(Instruction i, String operands) throws Exception {
            i.op1 = getOperand(operands);
            i.code = i.baseCode + i.op1.ordinal();
        }

    };

    private static Operand getOperand(String operands) throws Exception {
        Operand operand = map.get(operands.trim());
        if(operand == null) {
            throw new Exception("Operand expected. " + operands);
        }
        return operand;
    }
}
