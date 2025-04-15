package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.FileFormat;
import dsk.anotex.exporter.AnnotationExporter;
import dsk.anotex.exporter.ExporterFactory;
import dsk.anotex.importer.AnnotationImporter;
import dsk.anotex.importer.ImporterFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
     * Execute annotation extraction from file.
     * @param inputFile Input file name.
     * @param settings Additional export settings.
     * @param outputFile Output file name. If null - default will be used. If the output file already
     * exists, it will be overwritten.
     * @return The name of the created output file.
     */
    public String extractAnnotations(String inputFile, Map<String, Object> settings, String outputFile) {
        // Extract the annotations.
        AnnotatedDocument document = readAnnotations(inputFile);

        // Get appropriate exporter.
        String sFormat = (String) settings.get(Constants.EXPORT_FORMAT);
        FileFormat exportFormat = FileFormat.getByName(sFormat);
        if (sFormat == null) {
            // Use the default export format.
            exportFormat = getDefaultExportFormat();
        }
        AnnotationExporter exporter = ExporterFactory.createExporter(exportFormat);

        // Write the output.
        if (outputFile == null) {
            // Use default output file.
            outputFile = inputFile + exportFormat.getExtension();
        }
        try (Writer output = getOutputWriter(outputFile)) {
            exporter.export(document, settings, output);
        }
        catch (IOException e) {
            throw new RuntimeException("Extraction error", e);
        }
        return outputFile;
    }

    /**
     * Read annotations from given document file.
     * @param fileName Document file name.
     * @return Document annotations.
     */
    public AnnotatedDocument readAnnotations(String fileName) {
        FileFormat format = detectFileFormat(fileName);
        AnnotationImporter importer = ImporterFactory.createImporter(format);
        AnnotatedDocument document = importer.readAnnotations(fileName);
        postProcess(document);
        return document;
    }

    /**
     * Get the default export format.
     * @return Export format.
     */
    protected FileFormat getDefaultExportFormat() {
        return FileFormat.MARKDOWN;
    }

    /**
     * Get output writer for specified input file.
     * @param outputFile Output file name.
     * @return Output writer.
     */
    protected Writer getOutputWriter(String outputFile) {
        File outFile = new File(outputFile);

        // Create necessary directories fore the output path.
        File outFileDir = outFile.getParentFile();
        if (outFileDir != null) {
            outFileDir.mkdirs();
        }

        // Crate buffered file writer.
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outFile.toPath()),
                StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer;
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
        return formats.get(extension);
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
    @SuppressWarnings("unused")
    protected void postProcess(AnnotatedDocument document) {
    }

}

