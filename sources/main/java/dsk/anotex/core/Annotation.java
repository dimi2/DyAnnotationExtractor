package dsk.anotex.core;

import java.io.Serializable;

/**
 * Represents document annotation (highlight/comment). It is independent from the document format.
 */
public class Annotation implements Serializable {
    protected String text;

    public Annotation() {
    }

    public Annotation(String text) {
        this();
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" + text + '}';
    }
}
