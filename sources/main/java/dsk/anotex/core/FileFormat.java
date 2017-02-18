package dsk.anotex.core;

/**
 * File format enumeration.
 */
public enum FileFormat {
    PDF("Pdf", ".pdf"),
    MARKDOWN("Markdown", ".md");

    String name;
    String extension;

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    FileFormat(String name, String fileExtension) {
        this.name = name;
        this.extension = fileExtension;
    }

    public static FileFormat getByName(String name) {
        FileFormat match = null;
        for (FileFormat v : values()) {
            if (v.getName().equals(name)) {
                match = v;
                break;
            }
        } //
        return match;
    }
}
