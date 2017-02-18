package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class AnnotationExtractorTest extends TestBase {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMissingFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        extractor.extractAnnotations(resDir + "/Missing.pdf");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnsupportedFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        extractor.extractAnnotations(resDir + "/Test_Pdf_4.pdf");
    }

    @Test
    public void testHighlightingOnly() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.extractAnnotations(resDir + "/Test_Pdf_5.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.get(0);
        assertEquals("One Two", annot.getText());
        assertEquals(1, annotations.size());
    }

}
