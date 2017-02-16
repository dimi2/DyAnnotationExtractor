package dsk.anotex.export;

/**
 * Annotation exporter factory.
 */
public class ExporterFactory {

    /*
     * Prevent instance creation.
     */
    private ExporterFactory() {
    }

    /**
     * Create annotation exporter for specified file format.
     * @param format Desired file format.
     * @return Exporter instance for this format.
     */
    public static AnnotationExporter createExporter(ExportFormat format) {
        AnnotationExporter exporter;
        if (ExportFormat.MARKDOWN == format) {
            exporter = new MarkdownExporter();
        }
        else {
            String message = String.format("Unsupported export format '%s'", format);
            throw new IllegalArgumentException(message);
        }
        return exporter;
    }

}
