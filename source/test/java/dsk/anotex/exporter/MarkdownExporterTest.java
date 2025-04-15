package dsk.anotex.exporter;

import dsk.anotex.TestBase;
import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarkdownExporterTest extends TestBase {

    @Test
    public void testExport1() {
        MarkdownExporter exporter = new MarkdownExporter();
        AnnotatedDocument document = createDocument();
        StringWriter output = new StringWriter(256);
        exporter.export(document, new HashMap<>(), output);
        String sResult = "# Title1 #\n"
            + "\n"
            + "\n"
            + "Text1\n"
            + "Text2\n";
        String s = output.toString().replace("\r\n", "\n");
        assertEquals(sResult, s);
    }

    protected AnnotatedDocument createDocument() {
        AnnotatedDocument document = new AnnotatedDocument();
        document.setTitle("Title1");
        Annotation annot1 = new Annotation("Text1");
        Annotation annot2 = new Annotation("Text2");
        document.setAnnotations(Arrays.asList(annot1, annot2));
        return document;
    }
}