package dsk.anotex;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class ConsoleRunnerTest extends TestBase {

    @BeforeMethod
    public void beforeEach() {
        cleanTempDirectory();
    }

    @Test
    public void testExtraction1() {
        String inputFile = resDir + "/Test_Pdf_2.pdf";
        String outputFile = tempDir + "/Test_Pdf_2.pdf.md";
        ConsoleRunner.main(new String[]{"-input", inputFile, "-output", outputFile});
        String outputContent = readFile(outputFile);
        assertEquals(51, outputContent.length());
    }
}