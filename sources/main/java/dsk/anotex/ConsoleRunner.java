package dsk.anotex;

import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.export.AnnotationExporter;
import dsk.anotex.export.ExportFormat;
import dsk.anotex.export.ExporterFactory;
import dsk.anotex.util.CommandLineParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Console runner for the application.
 */
public class ConsoleRunner {
    // Recognized command line arguments.
    public static final String ARG_INPUT = "input";
    public static final String ARG_OUTPUT = "output";
    public static final String ARG_HELP = "help";

    public ConsoleRunner() {
    }

    /**
     * Execute annotation extraction from file.
     * @param inputFile Input file name.
     * @param settings Additional export settings.
     * @param outputFile Output file name. If null - default will be used. If the output file already
     * exists, it will be overwritten.
     */
    public void extract(String inputFile, Map<String, Object> settings, String outputFile) {
        // Read the annotations.
        AnnotationExtractor extractor = new AnnotationExtractor();
        AnnotatedDocument document = extractor.readAnnotations(inputFile);

        // Get appropriate exporter.
        String sFormat = (String) settings.get(Constants.EXPORT_FORMAT);
        ExportFormat exportFormat = ExportFormat.getByName(sFormat);
        AnnotationExporter exporter = ExporterFactory.createExporter(exportFormat);

        // Write the output.
        try (Writer output = getOutputWriter(outputFile)) {
            exporter.export(document, settings, output);
            output.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Extraction error", e);
        }
    }

    /**
     * Get the default export format.
     * @return Export format.
     */
    protected ExportFormat getDefaultExportFormat() {
        return ExportFormat.MARKDOWN;
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
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer;
    }

    /**
     * Print message to the console.
     * @param message The message.
     */
    protected void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Print error message to the console.
     * @param error The error message.
     */
    protected void printError(String error) {
        System.err.println(error);
        System.err.flush();
    }

    /**
     * Get the application start message.
     * @return Start message.
     */
    protected String getStartMessage() {
        return String.format("%s %s (document annotation extractor)", Constants.APP_NAME,
            Constants.APP_VERSION);
    }

    /**
     * Get information about the supported command line arguments.
     * Override to add your application specific parameters.
     * @return Command line arguments description.
     */
    protected String getHelpMessage() {
        return "Usage:\n"
            + String.format("DyAnnotationExtractor -%s <inputFile> -%s <outputFile>\n",
                ARG_INPUT, ARG_OUTPUT)
            + "where:\n"
            + "<inputFile> = input file name.\n"
            + "<outputFile> = output file name (optional).\n"
            + "additional arguments:\n"
            + String.format("-%s : Prints the supported command line arguments.\n", ARG_HELP);
    }

    /**
     * Execution entry point.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Start the application.
        ConsoleRunner runner = new ConsoleRunner();
        runner.printMessage(runner.getStartMessage());

        // Parse the command line.
        CommandLineParser parser = new CommandLineParser(args);
        parser.parseArguments(args);

        String inputFile = parser.getArgumentValue(ARG_INPUT);
        if ((inputFile != null)) {
            HashMap<String, Object> settings = new HashMap<>();
            // Currently we support only one export format.
            ExportFormat exportFormat = runner.getDefaultExportFormat();
            settings.put(Constants.EXPORT_FORMAT, exportFormat.getName());
            // Retrieve the output file name.
            String outputFile = parser.getArgumentValue(ARG_OUTPUT);
            if (outputFile == null) {
                // Use default output file name.
                outputFile = inputFile + exportFormat.getExtension();
            }
            // Execute the annotation extraction.
            runner.extract(inputFile, settings, outputFile);
        }
        else {
            // Print additional information.
            if (parser.hasArgument(ARG_HELP)) {
                runner.printMessage(runner.getHelpMessage());
            }
            else {
                runner.printError("Error: Invalid command line argument(s)");
                runner.printMessage(runner.getHelpMessage());
            }
        }
    }
}
