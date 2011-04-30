package com.mycompany.project.client.test;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.mycompany.project.client.Exe;


public class InstructionTests extends GWTTestCase {

    private Exe exe = new Exe();

    @Override
    public String getModuleName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void testArithmetic() throws Exception {
        String text = "mvi a,93h\n mvi  add c";
        exe.compileCode(text);
        exe.step();
        assertTrue(true);
    }
}
