package iit.valentinpichavant.multi_notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final int ADD_REQ = 1;

    private ArrayList<Note> listOfNotes = new ArrayList<>();

    private RecyclerView recyclerView;

    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(listOfNotes, this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new JsonLoadAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, getString(R.string.about_choice), Toast.LENGTH_SHORT).show();
                Intent intentAboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAboutActivity);
                return true;
            case R.id.add_note:
                Toast.makeText(this, getString(R.string.add_choice), Toast.LENGTH_SHORT).show();
                Intent intentAddNoteActivity = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intentAddNoteActivity, ADD_REQ);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new JsonSaveAsyncTask().execute();
    }

    private void loadNotes() {
        Log.d(TAG, "loadNote: Loading JSON File");
        listOfNotes.clear();
        try (JsonReader reader = new JsonReader(new InputStreamReader(getApplicationContext().openFileInput(getString(R.string.file_name)), getString(R.string.encoding)))) {
            reader.beginArray();
            while (reader.hasNext()) {
                Note note = readNote(reader);
                Log.d(TAG, note.toString());
                listOfNotes.add(note);
            }
            reader.endArray();
            notesAdapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Note readNote(JsonReader reader) throws IOException {
        Note note = new Note();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("date")) {
                try {
                    note.setDate(new SimpleDateFormat(getString(R.string.date_format), Locale.US).parse(reader.nextString()));
                } catch (ParseException pse) {
                    Log.d(TAG, pse.getMessage());
                }
            } else if (name.equals("content")) {
                note.setContent(reader.nextString());
            } else if (name.equals("title")) {
                note.setTitle(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return note;
    }

    private boolean saveNotes() {
        Log.d(TAG, "saveNote: Saving JSON File");
        boolean success = false;
        if (listOfNotes != null) {
            if (!listOfNotes.isEmpty()) {
                try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE), getString(R.string.encoding)))) {
                    writer.setIndent("  ");
                    writer.beginArray();
                    for (Note note : listOfNotes) {
                        saveNote(writer, note);
                    }
                    writer.endArray();
                    success = true;
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
        saveNotesToString();
        return success;
    }

    private void saveNotesToString() {
        Log.d(TAG, "saveNote: Saving JSON File");
        if (listOfNotes != null) {
            if (!listOfNotes.isEmpty()) {
                StringWriter sw = new StringWriter();
                try (JsonWriter writer = new JsonWriter(sw)) {
                    writer.setIndent("  ");
                    writer.beginArray();
                    for (Note note : listOfNotes) {
                        saveNote(writer, note);
                    }
                    writer.endArray();
                } catch (Exception e) {
                    e.getStackTrace();
                }
                Log.d(TAG, "saveNote JSON:\n" + sw.toString());
            }
        }
    }

    private void saveNote(JsonWriter writer, Note note) throws IOException {
        writer.beginObject();
        writer.name("date").value(new SimpleDateFormat(getString(R.string.date_format), Locale.US).format(note.getDate()));
        writer.name("title").value(note.getTitle());
        writer.name("content").value(note.getContent());
        writer.endObject();
    }

    @Override
    public void onClick(View v) {
        Intent intentAddNoteActivity = new Intent(MainActivity.this, AddNoteActivity.class);
        int pos = recyclerView.getChildLayoutPosition(v);
        intentAddNoteActivity.putExtra(getString(R.string.old_note), listOfNotes.get(pos));
        startActivityForResult(intentAddNoteActivity, ADD_REQ);
        Toast.makeText(v.getContext(), getString(R.string.edit_note), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        backDialog(listOfNotes.get(pos));
        Toast.makeText(v.getContext(),getString(R.string.delete_note), Toast.LENGTH_SHORT).show();
        return true;
    }

    public void backDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Note '" + note.getTitle() + "'");
        builder.setTitle(getString(R.string.delete_note_confirm));
        builder.setIcon(R.drawable.ic_save_white_48dp);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listOfNotes.remove(note);
                notesAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQ) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra(getString(R.string.note));
                if (note != null) {
                    Log.d(TAG, "onActivityResult: User Note: " + note);
                    Note oldNote = (Note) data.getSerializableExtra(getString(R.string.old_note));
                    if (oldNote != null) {
                        listOfNotes.remove(oldNote);
                    }
                    listOfNotes.add(0, note);
                    notesAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onActivityResult: User didn't edit the note");
                }
            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }
        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

    public class JsonSaveAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            saveNotes();
            return null;
        }
    }

    public class JsonLoadAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            loadNotes();
            return null;
        }
    }
}
