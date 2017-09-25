package com.jazasoft.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdzahidraza on 25/09/17.
 */
public class ProcessUtils {

    public static final String EXIT_CODE = "exitCode";
    public static final String CONSOLE_OUTPUT = "consoleOutput";
    public static final String CONSOLE_ERROR = "consoleError";


    public static Process createProcess(String... commands) throws IOException {
        return createProcess(null,null, commands);
    }

    public static Process createProcess(File dir, String... commands) throws IOException {
        return createProcess(dir, null, commands);
    }

    public static Process createProcess(Map<String, String> env, String... commands) throws IOException {
        return createProcess(null, env, commands);
    }

    public static Process createProcess(File dir, Map<String, String> env, String... commands) throws IOException {
        return createProcess(dir, env, null, commands);
    }

    /**
     * Create Process
     * @param dir Working directory. if null, current directory is directory in which java process is running
     * @param env Environment variables that will be available to process.
     * @param redirectFile File to which InputStream and ErrorStream should be redirected to. if null, redirected to PIPE
     * @param commands
     * @return
     * @throws IOException
     */
    public static Process createProcess(File dir, Map<String, String> env, File redirectFile, String... commands) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(commands);
        Map<String,String> env2 = builder.environment();
        if (env != null) env2.putAll(env);
        if (dir != null) builder.directory(dir);
        if (redirectFile != null) {
            builder.redirectErrorStream(true);
            builder.redirectInput(ProcessBuilder.Redirect.to(redirectFile));
        }
        return builder.start();
    }

    /**
     * Execute Process and get result.
     * @param process process to execute
     * @return Returns map of process results. keys are: {@code exitCode}, {@code consoleOutput}, {@code consoleError}
     */
    public static Map<String, Object> execute(Process process) {
        Map<String, Object> result = new HashMap<>();
        try {
            int exitCode = process.waitFor();
            result.put(EXIT_CODE, exitCode);
            if (process.getInputStream().available() != 0) {
                result.put(CONSOLE_OUTPUT, readStream(process.getInputStream()));
            }
            if (process.getErrorStream().available() != 0) {
                result.put(CONSOLE_ERROR, readStream(process.getErrorStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
