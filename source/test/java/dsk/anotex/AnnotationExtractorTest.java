package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnotationExtractorTest extends TestBase {

    @Test
    public void testMissingFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            extractor.readAnnotations(resDir + "/Missing.pdf");
        });
    }

    @Test
    public void testUnsupportedFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            extractor.readAnnotations(resDir + "/Test_Pdf_4.pdf");
        });
    }

    @Test
    public void testHighlightingOnly() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(resDir + "/Test_Pdf_5.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.getFirst();
        assertEquals("One Two", annot.getText());
        assertEquals(1, annotations.size());
    }

}
