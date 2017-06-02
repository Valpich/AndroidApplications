package iit.valentinpichavant.assignement2;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotePadActivity extends AppCompatActivity {

    private static final String TAG = "NotePadActivity";

    private TextView textViewDate;

    private EditText editTextNote;

    private Note note;

    private String textUnedited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pad);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        editTextNote = (EditText) findViewById(R.id.editTextNote);
    }

    @Override
    protected void onResume() {
        super.onStart();
        note = loadNote();
        if (note != null) {
            textViewDate.setText(new SimpleDateFormat(getString(R.string.date_format), Locale.US).format(note.getDate()));
            editTextNote.setText(note.getNote());
            textUnedited = note.getNote();
        }
    }

    private Note loadNote() {
        Log.d(TAG, "loadFile: Loading Serialized Note");
        note = new Note();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(getApplicationContext().openFileInput(getString(R.string.file_name)))) {
            note = (Note) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_notes), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            note.setDate(new SimpleDateFormat(getString(R.string.date_format), Locale.US).parse(new Date().toString()));
        } catch (ParseException parseException) {
            Log.d(TAG, "onPause: Failed to parse the date, current date is now the date.");
            note.setDate(new Date());
        }
        note.setNote(editTextNote.getText().toString());
        saveNote();
    }

    private void saveNote() {
        Log.d(TAG, "saveNote: Saving Serialized Note");
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE))) {
            objectOutputStream.writeObject(note);
            Toast.makeText(this, getString(R.string.notes_saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
