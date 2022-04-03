package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {

    private final LocalDateTime timestamp;
    private final String dateWritten;
    private final String authorName;
    private final String contents;

    public Comment(String authorName, String contents) {
        this.timestamp = LocalDateTime.now();
        this.dateWritten = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.authorName = authorName;
        this.contents = contents;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDateWritten() { return this.dateWritten; }

    public String getAuthorName() {
        return authorName;
    }

    public String getContents() {
        return contents;
    }
}
