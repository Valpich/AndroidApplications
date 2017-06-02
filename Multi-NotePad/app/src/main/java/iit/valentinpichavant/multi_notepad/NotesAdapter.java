package iit.valentinpichavant.multi_notepad;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NoteCardViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<Note> noteList;
    private MainActivity mainActivity;

    public NotesAdapter(List<Note> noteList, MainActivity mainActivity) {
        this.noteList = noteList;
        this.mainActivity = mainActivity;
    }

    @Override
    public NoteCardViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NoteCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteCardViewHolder holder, int position) {
        Log.d(TAG, "onCreateViewHolder: BINDING");
        Note note = noteList.get(position);
        holder.textViewTitle.setText(note.getTitle());
        holder.textViewDate.setText(new SimpleDateFormat(mainActivity.getString(R.string.date_format), Locale.US).format(note.getDate()));
        String content = note.getContent();
        if (content.length() <= 80) {
            holder.textViewContent.setText(content);
        } else {
            String noteWithDots = content.substring(0, 80) + "...";
            holder.textViewContent.setText(noteWithDots);
        }
    }
        @Override
    public int getItemCount() {
        return noteList.size();
    }

}