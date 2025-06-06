package dsk.anotex.importer;

import dsk.anotex.TestBase;
import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PdfAnnotationImporterTest extends TestBase {

    @Test
    public void testCyrillicAnnotation() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();
        AnnotatedDocument document = importer.readAnnotations(resDir + "/Test_Pdf_1.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.getFirst();
        assertEquals("\u041f\u0435\u0442", annot.getText()); // Пет ("five" in Cyrillic).
        assertEquals(1, annotations.size());
    }

    @Test
    public void testOneAnnotation() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();
        AnnotatedDocument document = importer.readAnnotations(resDir + "/Test_Pdf_2.pdf");
        assertEquals("Title2", document.getTitle());
        assertEquals("Subject2", document.getSubject());
        assertEquals("Author2", document.getAuthor());
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot = annotations.getFirst();
        assertEquals("Two", annot.getText());
        assertEquals(1, annotations.size());
    }

    @Test
    public void testComments() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();
        AnnotatedDocument document = importer.readAnnotations(resDir + "/Test_Pdf_3.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot1 = annotations.get(0);
        assertEquals("Four", annot1.getText());
        Annotation annot2 = annotations.get(1);
        assertEquals("Five", annot2.getText());
        Annotation annot3 = annotations.get(2);
        assertEquals("Six", annot3.getText());
        assertEquals(3, annotations.size());
    }
    
    @Test
    public void testStripUnwantedChunks() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();

        String res1 = importer.stripUnwantedChunks("Be them. W");
        assertEquals("Be them.", res1);

        String res2 = importer.stripUnwantedChunks("o? When");
        assertEquals("When", res2);

        String res3 = importer.stripUnwantedChunks("I can be");
        assertEquals("I can be", res3);

        String res4 = importer.stripUnwantedChunks("\"Awesome!\"");
        assertEquals("Awesome!", res4);
    }

    @Test
    public void testHighlightingBoundaries() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();
        AnnotatedDocument document = importer.readAnnotations(resDir + "/Test_Pdf_6.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot1 = annotations.getFirst();
        assertEquals("seven eight nine ten eleven twelve thirteen fourteen fifteen", annot1.getText());
    }

    @Test
    public void testHighlightingWithContent() {
        PdfAnnotationImporter importer = new PdfAnnotationImporter();
        AnnotatedDocument document = importer.readAnnotations(resDir + "/Test_Pdf_7.pdf");
        List<Annotation> annotations = document.getAnnotations();
        Annotation annot1 = annotations.getFirst();
        assertEquals("The programs that a home user needs are email, web browser, pdf file viewer, " +
            "video an music playback software as well as, office program including spreadsheet, " +
            "word processing and presentation graphics. Today, cloud services, " +
            "web calls and other social", annot1.getText());
    }
}