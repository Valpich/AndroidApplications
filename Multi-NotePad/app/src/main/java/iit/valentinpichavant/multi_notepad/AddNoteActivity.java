package iit.valentinpichavant.multi_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextNoteTitle;

    private EditText editTextNoteContent;

    private Note noteTmp;

    private StringBuilder exit = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTextNoteTitle = (EditText) findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = (EditText) findViewById(R.id.editTextNoteContent);
        noteTmp = (Note) getIntent().getSerializableExtra(getString(R.string.old_note));
        if (noteTmp != null) {
            editTextNoteTitle.setText(noteTmp.getTitle());
            editTextNoteContent.setText(noteTmp.getContent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Save note
        Note note = new Note();
        note.setTitle(editTextNoteTitle.getText().toString());
        note.setContent(editTextNoteContent.getText().toString());
        Intent data = new Intent();
        if (!note.equals(noteTmp)) {
            if (!note.getTitle().equals("")) {
                note.setDate(new Date());
                data.putExtra(getString(R.string.note), note);
                data.putExtra(getString(R.string.old_note), noteTmp);
            } else {
                Toast.makeText(this, getString(R.string.no_title), Toast.LENGTH_SHORT).show();
            }
        }
        setResult(RESULT_OK, data);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent data;
        switch (exit.toString()) {
            case "true":
                data = new Intent();
                Note note = new Note();
                note.setTitle(editTextNoteTitle.getText().toString());
                note.setContent(editTextNoteContent.getText().toString());
                if (!note.equals(noteTmp)) {
                    if (!note.getTitle().equals("")) {
                        note.setDate(new Date());
                        data.putExtra(getString(R.string.note), note);
                        data.putExtra(getString(R.string.old_note), noteTmp);
                    } else {
                        Toast.makeText(this, getString(R.string.no_title), Toast.LENGTH_SHORT).show();
                    }
                }
                setResult(RESULT_OK, data);
                super.onBackPressed();
                break;
            case "false":
                data = new Intent();
                setResult(RESULT_OK, data);
                super.onBackPressed();
                break;
            default:
                backDialog();
                break;
        }

    }

    public void backDialog() {
        String title = editTextNoteTitle.getText().toString();
        final AddNoteActivity activity = this;
        exit = new StringBuilder();
        if (!title.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.save_note_msg) + title + "'");
            builder.setTitle(getString(R.string.note_not_save));
            builder.setIcon(R.drawable.ic_save_white_48dp);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.exit.append("true");
                    activity.onBackPressed();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.exit.append("false");
                    activity.onBackPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
