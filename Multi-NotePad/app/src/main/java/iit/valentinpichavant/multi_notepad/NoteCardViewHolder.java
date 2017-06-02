package iit.valentinpichavant.multi_notepad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Christopher on 1/30/2017.
 */

public class NoteCardViewHolder extends RecyclerView.ViewHolder {

    public TextView textViewTitle;
    public TextView textViewDate;
    public TextView textViewContent;

    public NoteCardViewHolder(View view) {
        super(view);
        textViewTitle = (TextView) view.findViewById(R.id.textViewNoteTitle);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewContent = (TextView) view.findViewById(R.id.textViewContent);
    }

}
