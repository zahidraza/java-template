package com.jazasoft.mtdb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by mdzahidraza on 27/06/17.
 */
public class ScriptUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptUtils.class);

    public static int execute(String... command) {
        File dir = new File(Utils.getAppHome() + File.separator + "bin");
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(dir);
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            LOGGER.error("Error: {}", readStream(process.getErrorStream()));
            LOGGER.info("Status: {}", readStream(process.getInputStream()));
            return exitCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static String readStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.getProperty("line.separator"));
            }
        }finally {
            reader.close();
        }
        return builder.toString();
    }
}
