package iit.valentinpichavant.stock_watch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class StockAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "CountryAdapter";
    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> stockList, MainActivity ma) {
        this.stockList = stockList;
        mainAct = ma;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        final boolean positiveChange = stock.getPriceChangePercentage() >= 0;
        holder.mame.setText(stock.getCompany());
        holder.symbol.setText(stock.getSymbol());
        holder.lastTradePrice.setText(stock.getLastTradePrice().toString());
        if (positiveChange) {
            holder.mame.setTextColor(Color.rgb(0, 255, 0));
            holder.symbol.setTextColor(Color.rgb(0, 255, 0));
            holder.lastTradePrice.setTextColor(Color.rgb(0, 255, 0));
            holder.variation.setText("▲ " + stock.getPriceChangeAmmount() + " (" + stock.getPriceChangePercentage() + "%)");
            holder.variation.setTextColor(Color.rgb(0, 255, 0));
        } else {
            holder.mame.setTextColor(Color.rgb(255, 0, 0));
            holder.symbol.setTextColor(Color.rgb(255, 0, 0));
            holder.lastTradePrice.setTextColor(Color.rgb(255, 0, 0));
            holder.variation.setText("▼ " + stock.getPriceChangeAmmount() + " (" + stock.getPriceChangePercentage() + "%)");
            holder.variation.setTextColor(Color.rgb(255, 0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
