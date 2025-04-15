package dsk.anotex.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents annotated document. It is independent of the original document format.
 */
public class AnnotatedDocument implements Serializable {
    protected String title;
    protected String subject;
    protected String author;
    protected List<String> keywords;
    protected List<Annotation> annotations;

    public AnnotatedDocument() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Annotation> getAnnotations() {
        if (annotations == null) {
            annotations = new LinkedList<>();
        }
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<String> getKeywords() {
        if (keywords == null) {
            keywords = new LinkedList<>();
        }
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "{" + title + '}';
    }
}
