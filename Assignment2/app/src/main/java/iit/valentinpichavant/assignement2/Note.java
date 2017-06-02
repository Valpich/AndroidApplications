package iit.valentinpichavant.assignement2;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * Created by valentinpichavant on 2/4/17.
 */

public class Note implements Externalizable {

    private static final long serialVersionUID = 1L;
    private static final byte INTERNAL_VERSION = 1;

    private Date date;
    private String note;

    public Note() {
        // To make sur that if there is no data the application start with the current date and an empty note
        date = new Date();
        note = "";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(INTERNAL_VERSION);
        out.writeObject(date);
        out.writeObject(note);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int version = in.readByte();
        if (version == 1){
            date = (Date) in.readObject();
            note = (String) in.readObject();
        }
    }
}
