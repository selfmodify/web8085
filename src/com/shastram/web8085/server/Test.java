package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.shastram.web8085.client.Exe;


public class Test {

    @org.junit.Test
    public void testeArithmetic( ) throws Exception {
        File f = new File(".");
        String p = f.getCanonicalPath();
        BufferedReader in = new BufferedReader(new FileReader("test_cases/arithmetic_tests.85"));
        String line;
        StringBuffer buffer = new StringBuffer();
        while((line = in.readLine()) != null) {
            buffer.append(line);
        }
        Exe exe = new Exe();
        exe.compileCode(buffer.toString());
        exe.step();
    }
}
