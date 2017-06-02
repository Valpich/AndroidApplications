package iit.valentinpichavant.multi_notepad;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by valentinpichavant on 2/4/17.
 */

public class Note implements Serializable {

    private Date date;
    private String content;
    private String title;

    public Note() {
        // To make sur that if there is no data the application start with the current date and an empty content
        date = new Date();
        content = "";
        title = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Note{" +
                "date=" + date +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (content != null ? !content.equals(note.content) : note.content != null) return false;
        return title != null ? title.equals(note.title) : note.title == null;

    }

}
