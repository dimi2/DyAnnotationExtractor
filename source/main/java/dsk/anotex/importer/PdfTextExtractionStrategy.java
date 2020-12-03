package dsk.anotex.importer;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

/**
 * Pdf text extraction strategy, which cuts the text chunks crossing the extraction area.
 * By default, IText library does not cut such text snippets, so we do it here.
 */
public class PdfTextExtractionStrategy extends LocationTextExtractionStrategy {
    protected Rectangle extractionArea;

    public PdfTextExtractionStrategy(Rectangle extractionArea) {
        super();
        this.extractionArea = extractionArea;
    }

    @Override
    public void eventOccurred(IEventData eventData, EventType eventType) {
        if (EventType.RENDER_TEXT == eventType) {
            TextRenderInfo data = (TextRenderInfo) eventData;
            // Split the text snippet to chars.
            for (TextRenderInfo renderInfo : data.getCharacterRenderInfos()) {
                // Get the char rendering boundaries.
                Rectangle charArea = new CharacterRenderInfo(renderInfo).getBoundingBox();
                if (isInsideExtractionArea(charArea)) {
                    // Extract this char.
                    super.eventOccurred(renderInfo, eventType);
                }
            } //
        }
    }

    /**
     * Check if the rendered text intersects the extraction area.
     * @param textArea Text rendering area.
     * @return True if the text is inside.
     */
    protected boolean isInsideExtractionArea(Rectangle textArea) {
        return extractionArea.contains(textArea);
    }
}
