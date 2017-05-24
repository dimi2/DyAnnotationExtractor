package dsk.anotex.importer;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredTextEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import dsk.anotex.core.AnnotatedDocument;
import dsk.anotex.core.Annotation;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Import annotations form PFD files.
 */
public class PdfAnnotationImporter implements AnnotationImporter {

    public AnnotatedDocument readAnnotations(String fileName) {
        // Check the file existence.
        File file = new File(fileName).getAbsoluteFile();
        if (!file.isFile()) {
            String message = String.format("File '%s' does not exist", file.getName());
            throw new IllegalArgumentException(message);
        }

        // Extract the annotations.
        PdfDocument pdfDocument = readDocument(file);
        return extractAnnotations(pdfDocument);
    }

    /**
     * Read PDF document from file.
     * @param file File name.
     * @return PDF document.
     */
    protected PdfDocument readDocument(File file) {
        PdfDocument document;
        try {
            document = new PdfDocument(new PdfReader(file.getAbsolutePath()));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return document;
    }

    /**
     * Extract annotations from given PDF document.
     * @param pdfDocument PDF document.
     * @return Extracted annotations.
     */
    protected AnnotatedDocument extractAnnotations(PdfDocument pdfDocument) {
        AnnotatedDocument document = new AnnotatedDocument();
        PdfDocumentInfo pdfInfo = pdfDocument.getDocumentInfo();
        document.setTitle(pdfInfo.getTitle());
        document.setSubject(pdfInfo.getSubject());
        document.setAuthor(pdfInfo.getAuthor());
        List<String> keywords = convertToKeywords(pdfInfo.getKeywords());
        document.setKeywords(keywords);

        List<Annotation> annotations = new LinkedList<>();
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            PdfPage page = pdfDocument.getPage(i);
            for (PdfAnnotation pdfAnnotation : page.getAnnotations()) {
                Annotation annotation = convertAnnotation(pdfAnnotation);
                if (annotation != null) {
                    annotations.add(annotation);
                }
            } //
        } //
        document.setAnnotations(annotations);

        return document;
    }

    /**
     * Convert document annotation to independent format.
     * @param pdfAnnotation Annotation to be converted.
     * @return Converted annotation.
     */
    protected Annotation convertAnnotation(PdfAnnotation pdfAnnotation) {
        String text = null;
        PdfArray textCoordinates = pdfAnnotation.getRectangle();
        PdfString pdfText = pdfAnnotation.getContents();
        if (pdfText == null) {
            // The text is not included in the annotation content - extract from highlighted text.
            if (PdfName.Highlight.equals(pdfAnnotation.getSubtype())) {
                // We should use 'getQuadPoints()' here, but 'getRectangle()' gives better result
                // (because of variances in PDF standard implementations of QuadPoints).
                Rectangle highlightedArea = toRectangle(textCoordinates);
                ITextExtractionStrategy textFilter = new FilteredTextEventListener(
                    new LocationTextExtractionStrategy(), new TextRegionEventFilter(highlightedArea));
                String highlightedText = PdfTextExtractor.getTextFromPage(pdfAnnotation.getPage(),
                    textFilter);
                text = normalizeHighlightedText(highlightedText);
            }
        }
        else {
            // The text is included in the annotation content - use it directly.
            if (pdfText.getEncoding() == null) {
                text = pdfText.toUnicodeString();
            }
            else {
                text = pdfText.getValue();
            }
        }

        Annotation annotation = null;
        if (text != null) {
            annotation = new Annotation();
            text = stripUnwantedChunks(text);
            text = removePollutionChars(text);
            annotation.setText(text);
        }
        return annotation;
    }

    /**
     * Convert comma separated string to list of keywords.
     * @param sKeywords String to be converted.
     * @return List of keywords.
     */
    protected List<String> convertToKeywords(String sKeywords) {
        List<String> keywords;
        if ((sKeywords != null) && !sKeywords.isEmpty()) {
            // The string can be surrounded with double quotes.
            sKeywords = stripDoubleQuotes(sKeywords);
            // Split on comma (and trim around it).
            String[] words = sKeywords.split(" ?, ?");
            keywords = Arrays.asList(words);
        }
        else {
            keywords = new LinkedList<>();
        }
        return keywords;
    }

    /**
     * Strip double quotes enclosing string. For example:
     * <pre>
     *     "Flower" becomes Flower
     * </pre>
     * @param text Text to be stripped.
     * @return Stripped text.
     */
    protected String stripDoubleQuotes(String text) {
        String st = text;
        char dQuota = '"';
        int endPos = text.length() - 1;
        if ((text.charAt(0) == dQuota)
            && (text.charAt(endPos) == dQuota)) {
            // Remove the surrounding chars.
            st = text.substring(1, endPos).trim();
        }
        return st;
    }

    /**
     * Convert array of points, describing to rectangular shape.
     * The points describe two diagonally opposite points: lower-left / upper-right.
     * @param rectPoints Quad points.
     * @return Corresponding rectangle.
     */
    protected Rectangle toRectangle(PdfArray rectPoints) {
        float x1 = ((PdfNumber)rectPoints.get(0)).floatValue();
        float y1 = ((PdfNumber)rectPoints.get(1)).floatValue();
        float x3 = ((PdfNumber)rectPoints.get(2)).floatValue();
        float y3 = ((PdfNumber)rectPoints.get(3)).floatValue();
        return new Rectangle(x1, y1, (x3 - x1), (y3 - y1));
    }

    /**
     * Normalize highlighted text - when retrieved from PDF renderer, it contains defects (like
     * additional spaces, inappropriate characters).
     * @param highlightedText Highlighted text.
     * @return Normalized text.
     */
    protected String normalizeHighlightedText(String highlightedText) {
        return highlightedText.replaceAll("\\s+", " ").replaceAll("[“”]", "\"");
    }

    /**
     * Strip unwanted character before or after the annotation (these chunks are PDF library issue).
     * @param text The text to strip.
     * @return Stripped text.
     */
    protected String stripUnwantedChunks(String text) {
        text = text.replaceFirst("^\\p{javaLowerCase}?[.?!]? ", "")
            .replaceFirst(" \\p{IsAlphabetic}?$", "");
        text = stripDoubleQuotes(text);
        return text;
    }

    /**
     * Remove the pollution characters from the annotation text. These characters appear, without being
     * part of the original text:
     * <ul>
     *     <li>Tab chars appear between words if the original text it aligned on both sides.</li>
     * </ul>
     * @param text The text to clean.
     * @return Cleaned text.
     */
    protected String removePollutionChars(String text) {
        text = text.replaceAll("\t", " ");
        text = stripDoubleQuotes(text);
        return text;
    }
}
