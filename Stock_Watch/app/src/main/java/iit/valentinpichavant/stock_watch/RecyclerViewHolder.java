package iit.valentinpichavant.stock_watch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {


    public TextView mame;
    public TextView symbol;
    public TextView lastTradePrice;
    public TextView variation;

    public RecyclerViewHolder(View view) {
        super(view);
        mame = (TextView) view.findViewById(R.id.textViewName);
        symbol = (TextView) view.findViewById(R.id.textViewSymbol);
        lastTradePrice = (TextView) view.findViewById(R.id.textViewLastTradePrice);
        variation = (TextView) view.findViewById(R.id.textViewVariation);
    }

}
