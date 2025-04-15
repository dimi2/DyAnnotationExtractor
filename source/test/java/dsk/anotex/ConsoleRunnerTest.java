package dsk.anotex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleRunnerTest extends TestBase {

    @BeforeEach
    public void beforeEach() {
        cleanTempDirectory();
    }

    @Test
    public void testExtraction1() {
        String inputFile = resDir + "/Test_Pdf_2.pdf";
        String outputFile = tempDir + "/Test_Pdf_2.pdf.md";
        ConsoleRunner.main(new String[]{"-input", inputFile, "-output", outputFile});
        String outputContent = readFile(outputFile);
        assertEquals("94d6378bf0eacfef6ec05e6b187673ac88f2d6ba4556acba584bb031f79f4ffa",
            calcChecksum(outputContent));
    }
}