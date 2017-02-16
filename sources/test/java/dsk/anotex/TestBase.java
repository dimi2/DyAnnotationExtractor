package dsk.anotex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Base functionality for unit tests.
 */
public abstract class TestBase {
    protected static File workDir;
    protected static File tempDir;
    protected static String resDir;
    protected static MessageDigest digester;

    public TestBase() {
        super();
        if (workDir == null) {
            workDir = setupWorkDirectory("work");
            tempDir = setupTempDirectory("temp");
            resDir = workDir + "/../resources/test";
        }
    }

    /**
     * Setup working directory to run the tests from.
     * @param dir Work directory name (relative to project root directory). Null = to use default.
     * @return The work directory.
     */
    protected File setupWorkDirectory(String dir) {
        File workDir;
        if (dir == null) {
            // Detect the application home directory.
            workDir = new File(getClass().getClassLoader().getResource(".").getFile()).getParentFile();
        }
        else {
            // Try the requested directory.
            workDir = new File(dir).getAbsoluteFile();
            if (!workDir.isDirectory()) {
                // Then use the current directory.
                workDir = new File("").getAbsoluteFile();
            }
        }

        // Change the work directory (note - this will not affect ).
        System.setProperty("user.dir", workDir.getAbsolutePath());
        return workDir;
    }

    /**
     * Setup temporary storage directory for tests.
     * @param dir Temporary directory name (relative to project root directory). Null = to use default.
     * @return The temp directory.
     */
    protected File setupTempDirectory(String dir) {
        File tempDir;
        if (dir == null) {
            tempDir = new File(workDir, "temp").getAbsoluteFile();
        }
        else {
            tempDir = new File(dir).getAbsoluteFile();
        }
        if (!tempDir.canWrite()) {
            // Not writable directory. Try the JVM temp directory.
            String systemTempDir = System.getProperty("java.io.tmpdir");
            tempDir = new File(systemTempDir).getAbsoluteFile();
        }
        tempDir.mkdirs();
        return tempDir;
    }

    /**
     * Clean the temporary directory.
     */
    protected void cleanTempDirectory() {
        if (tempDir != null) {
            if (tempDir.isDirectory()) {
                File[] files = tempDir.listFiles();
                if ((files != null) && (files.length > 0)) {
                    for (File file : files) {
                        removeDirectory(file);
                    } //
                }
            }
        }
    }

    /**
     * Remove specified directory with its sub-directories.
     * @param dir Directory name.
     */
    protected void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if ((files != null) && (files.length > 0)) {
                for (File file : files) {
                    removeDirectory(file);
                } //
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    /**
     * Read complete file into string. Works with UTF-8 encoding.
     * @param fileName Name of the file to read.
     * @return File content.
     */
    protected static String readFile(String fileName) {
        // The variant with 'Paths' is not used intentionally (it ignores work directory change).
        String ret = null;
        if (fileName != null) {
            StringBuilder content = new StringBuilder();
            File file = new File(fileName).getAbsoluteFile();
            int bufSize = (int) file.length();
            if (bufSize > 0) {
                try {
                    char[] buf = new char[bufSize];
                    Reader f = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    int read;
                    while ((read = f.read(buf, 0, bufSize)) != -1) {
                        content.append(buf, 0, read);
                        if (read < bufSize) {
                            break;
                        }
                    } //
                    f.close();
                }
                catch (IOException e) {
                    String message = String.format("Cannot read file '%s'", file);
                    throw new IllegalArgumentException(message, e);
                }
            }
            ret = content.toString();
        }
        return ret;
    }

    /**
     * Write specified string into file. Works with UTF-8 encoding.
     * @param fileName Desired file name.
     * @param fileContent File content.
     */
    protected static void writeFile(String fileName, String fileContent) {
        // The variant with 'Paths' is not used intentionally (it ignores work directory change).
        if ((fileName != null) && (fileContent != null)) {
            File file = new File(fileName).getAbsoluteFile();
            try {
                file.getParentFile().mkdirs();
                FileWriter f = new FileWriter(file);
                f.write(new String(fileContent.getBytes(StandardCharsets.UTF_8)));
                f.close();
            }
            catch (IOException e) {
                String message = String.format("Cannot write file '%s'", file);
                throw new IllegalArgumentException(message, e);
            }
        }
    }

}
