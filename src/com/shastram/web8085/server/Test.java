package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.FileReader;

import com.shastram.web8085.client.Exe;


public class Test {

    @org.junit.Test
    public void testeArithmetic( ) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader("test_cases/arithmetic_tests.85"));
        String line;
        StringBuffer buffer = new StringBuffer();
        while((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        Exe exe = new Exe();
        exe.compileCode(buffer.toString());

        // run until hlt is executed
        while(!exe.hltExecuted()) {
            exe.step();
        }
    }
}
