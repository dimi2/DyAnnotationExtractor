package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.FileFormat;
import dsk.anotex.importer.AnnotationImporter;
import dsk.anotex.importer.ImporterFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Document annotation extractor.
 */
public class AnnotationExtractor {
    protected Map<String, FileFormat> formats;

    public AnnotationExtractor() {
        super();
        formats = getKnownFileFormats();
    }

    /**
     * Extract annotations from given document file.
     * @param fileName Document file name.
     * @return Document annotations.
     */
    public AnnotatedDocument extractAnnotations(String fileName) {
        FileFormat format = detectFileFormat(fileName);
        AnnotationImporter importer = ImporterFactory.createImporter(format);
        AnnotatedDocument document = importer.readAnnotations(fileName);
        postProcess(document);
        return document;
    }

    /**
     * Detect the file format.
     * @param fileName Document file name.
     * @return Detected format, null for unknown formats.
     */
    protected FileFormat detectFileFormat(String fileName) {
        // We use the file name extension to determine the format.
        // Reading the first few bytes (signature) from the file would provide more reliable detection.
        // But this one is good enough, since the associated importer will parse the file anyway and will
        // detect if the file format is wrong (for example, PNG file, renamed with PDF extension).
        String extension = getFileExtension(fileName);
        FileFormat format = formats.get(extension);
        return format;
    }

    /**
     * Get file name extension of specified file. Example: for 'file1.ext' it will return '.ext'
     * @param fileName The file name.
     * @return File extension (in lowercase) or empty string if there is no extension.
     */
    protected String getFileExtension(String fileName) {
        String ret = "";
        if (fileName != null) {
            int idx = fileName.lastIndexOf('.');
            if (idx > 0 && (idx < fileName.length() - 1)) {
                ret = fileName.substring(idx).toLowerCase();
            }
        }
        return ret;
    }

    /**
     * Create mapping between file extensions and the known file formats.
     * @return The mapping.
     */
    protected Map<String, FileFormat> getKnownFileFormats() {
        TreeMap<String, FileFormat> mapping = new TreeMap<>();
        mapping.put(FileFormat.PDF.getExtension(), FileFormat.PDF);
        mapping.put(FileFormat.MARKDOWN.getExtension(), FileFormat.MARKDOWN);
        return mapping;
    }

    /**
     * Post-process annotated document. This is extension point.
     * @param document The annotated document.
     */
    protected void postProcess(AnnotatedDocument document) {
    }

}

