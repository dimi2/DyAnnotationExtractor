package dsk.anotex.importer;

import dsk.anotex.core.FileFormat;

/**
 * Annotation importer factory.
 */
public class ImporterFactory {

    /*
     * Prevent instance creation.
     */
    private ImporterFactory() {
    }

    /**
     * Create annotation importer for specified file format.
     * @param format Desired file format.
     * @return Importer instance for this format.
     */
    public static AnnotationImporter createImporter(FileFormat format) {
        AnnotationImporter importer;
        if (FileFormat.PDF == format) {
            importer = new PdfAnnotationImporter();
        }
        else {
            String message = String.format("Unsupported import format '%s'", format);
            throw new IllegalArgumentException(message);
        }
        return importer;
    }

}
