package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.mortbay.log.Log;

import com.shastram.web8085.client.Exe;

public class Test {

    private static Logger logger = Logger.getLogger(Test.class.getName());

    @org.junit.Test
    public void testeArithmetic() throws Exception {
        String testArithmetic = "arithmetic_tests.85";
        String loadStoreTests = "load_store.85";
        String testMov = "mov_tests.85";
        String testName = loadStoreTests;
        BufferedReader in = new BufferedReader(new FileReader("test_cases/" + testName));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        Exe exe = new Exe();
        try {
            exe.compileCode(buffer.toString());
            // run until hlt is executed
            while (!exe.hltExecuted()) {
                exe.step();
            }
            Log.info("Finished");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed with ", e);
            Assert.fail(e.getMessage());
        }
    }
}
