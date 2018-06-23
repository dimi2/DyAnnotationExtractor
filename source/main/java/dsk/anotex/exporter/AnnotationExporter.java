package dsk.anotex.exporter;

import dsk.anotex.core.AnnotatedDocument;

import java.io.Writer;
import java.util.Map;

/**
 * Interface for exporting annotated document to some standard format.
 */
public interface AnnotationExporter {

    /**
     * Export annotated document.
     * @param document The document to be converted.
     * @param context Conversion context.
     * @param output Stream where to write the output.
     */
    public void export(AnnotatedDocument document, Map<String, Object> context, Writer output);
}
