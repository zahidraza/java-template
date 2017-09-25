package com.jazasoft.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mdzahidraza on 25/09/17.
 */
@Test
public class ProcessUtilsTest {

    public void executeTest() throws IOException {
        Process process = ProcessUtils.createProcess("echo", "Hello", "World!");
        Map<String, Object> result = ProcessUtils.execute(process);
        Assert.assertEquals(result.get(ProcessUtils.EXIT_CODE), 0);
        Assert.assertEquals(result.get(ProcessUtils.CONSOLE_OUTPUT), "Hello World!\n");
        Assert.assertNull(result.get(ProcessUtils.CONSOLE_ERROR));
    }
}
