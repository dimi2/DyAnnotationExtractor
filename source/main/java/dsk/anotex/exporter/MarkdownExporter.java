package dsk.anotex.exporter;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Export annotated document to Markdown format.
 */
public class MarkdownExporter implements AnnotationExporter {

    @Override
    public void export(AnnotatedDocument document, Map<String, Object> context, Writer output) {
        String mdDocument = convert(document);
        try {
            output.write(mdDocument);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert annotated document to string in Markdown format.
     * @param document Document to convert.
     * @return The document as string.
     */
    protected String convert(AnnotatedDocument document) {
        final String BR = System.lineSeparator();
        // TODO: Use specialized Markdown library if the requirements evolve
        // (currently this would be overkill).
        StringBuilder buf = new StringBuilder(1024);
        buf.append("# ").append(document.getTitle()).append(" #");
        buf.append(BR);
        buf.append(BR);
        String subject = document.getSubject();
        if (subject != null) {
            buf.append("\"").append(subject).append("\"");
            buf.append(BR);
        }
        List<String> keywords = document.getKeywords();
        if (!keywords.isEmpty()) {
            buf.append(keywords);
            buf.append(BR);
        }
        buf.append(BR);
        for (Annotation annotation : document.getAnnotations()) {
            buf.append(annotation.getText());
            buf.append(BR);
        } //
        return buf.toString();
    }
}
