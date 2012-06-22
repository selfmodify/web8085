package com.shastram.web8085.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Ignore;
import org.mortbay.log.Log;

import com.shastram.web8085.client.Exe;
import com.shastram.web8085.client.MicroCode;

public class Test {

    private static Logger logger = Logger.getLogger(Test.class.getName());

    @Ignore
    public void testArithmetic() throws Exception {
        String testArithmetic = "arithmetic_tests.85";
        String loadStoreTests = "load_store.85";
        String testMov = "mov_tests.85";
        String testLxi = "lxi_tests.85";
        String testName = testMov;
        testFile(testName);
    }

    public void testFile(String testName) throws FileNotFoundException, IOException {
        String fullTestcaseName = "test_cases/" + testName;
        BufferedReader in = new BufferedReader(new FileReader(fullTestcaseName));
        String line;
        StringBuffer buffer = new StringBuffer();
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        Exe exe = new Exe();
        try {
            exe.compileCode(buffer.toString(), fullTestcaseName);
            // run until hlt is executed
            while (!exe.hltExecuted()) {
                exe.step();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed with ", e);
            Assert.fail(e.getMessage());
        }
    }

    private static String[] testNames = {
            //"temp_test.85",
    		"inout_test.85",
    		"interrupt_test.85",
    		"rst_test.85",
            "stack_test.85",
            "branch_test.85",
            "complement_test.85",
            "rotate_test.85",
            "logical_test.85",
            "compare_test.85",
            "arithmetic_tests.85",
            "load_store.85",
            "mov_tests.85",
            "lxi_tests.85",
    };

    public static String[] getTestNames() {
    	return testNames;
    }

    @org.junit.Test
    public void testAll() throws IOException {
        //tests = new String[] { "rst_test.85" }; 
        MicroCode.selfTest();
        for (String s : testNames) {
            testFile(s);
        }
        Log.info("Finished All tests");
    }
}
