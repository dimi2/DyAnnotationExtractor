package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class AnnotationExtractorTest extends TestBase {

    @Test
    public void testCyrillicAnnotation() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(resDir + "/Test_Pdf_1.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.get(0);
        assertEquals("\u041f\u0435\u0442", annot.getText()); // Пет (Cyrillic).
        assertEquals(1, annotations.size());
    }

    @Test
    public void testOneAnnotation() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(resDir + "/Test_Pdf_2.pdf");
        assertEquals("Title2", document.getTitle());
        assertEquals("Subject2", document.getSubject());
        assertEquals("Author2", document.getAuthor());
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.get(0);
        assertEquals("Two", annot.getText());
        assertEquals(1, annotations.size());
    }

    @Test
    public void testComments() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(resDir + "/Test_Pdf_3.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot1 = annotations.get(0);
        assertEquals("Four", annot1.getText());
        Annotation annot2 = annotations.get(1);
        assertEquals("Five", annot2.getText());
        Annotation annot3 = annotations.get(2);
        assertEquals("Six", annot3.getText());
        assertEquals(3, annotations.size());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMissingFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        extractor.readAnnotations(resDir + "/Missing.pdf");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnsupportedFile() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        extractor.readAnnotations(resDir + "/Test_Pdf_4.pdf");
    }

    @Test
    public void testHighlightingOnly() {
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(resDir + "/Test_Pdf_5.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.get(0);
        assertEquals("One Two", annot.getText());
        assertEquals(1, annotations.size());
    }

    @Test
    public void testStripUnwantedChunks() {
        AnnotationExtractor extractor = new AnnotationExtractor();

        String res1 = extractor.stripUnwantedChunks("Be them. W");
        assertEquals("Be them.", res1);

        String res2 = extractor.stripUnwantedChunks("o? When");
        assertEquals("When", res2);

        String res3 = extractor.stripUnwantedChunks("I can be");
        assertEquals("I can be", res3);

        String res4 = extractor.stripUnwantedChunks("\"Awesome!\"");
        assertEquals("Awesome!", res4);
    }
}
