package iit.valentinpichavant.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView officeName;
    public TextView mame;

    public RecyclerViewHolder(View view) {
        super(view);
        officeName = (TextView) view.findViewById(R.id.textViewRole);
        mame = (TextView) view.findViewById(R.id.textViewName);
    }

}
