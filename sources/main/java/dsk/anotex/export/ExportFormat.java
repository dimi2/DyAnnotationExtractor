package dsk.anotex.export;

/**
 * Export formats enumeration.
 */
public enum ExportFormat {
    MARKDOWN("Markdown", ".md");

    String name;
    String extension;

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    ExportFormat(String name, String fileExtension) {
        this.name = name;
        this.extension = fileExtension;
    }

    public static ExportFormat getByName(String name) {
        ExportFormat match = null;
        for (ExportFormat v : values()) {
            if (v.getName().equals(name)) {
                match = v;
                break;
            }
        } //
        return match;
    }
}
