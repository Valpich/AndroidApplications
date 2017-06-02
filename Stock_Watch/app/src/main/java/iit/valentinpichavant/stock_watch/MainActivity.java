package iit.valentinpichavant.stock_watch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static String stockURL = "http://www.marketwatch.com/investing/stock/";
    private List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StockAdapter mAdapter;
    private SwipeRefreshLayout swiper;
    private DatabaseHandler databaseHandler;
    private MainActivity mainActivity = this;
    private boolean loadFailed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.dumpLog();
        swiper = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshView);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkOn()) {
                    for (String[] stockArray : databaseHandler.loadStocks()) {
                        Stock stock = new Stock(stockArray[0], stockArray[1], 0.0, 0.0, 0.0);
                        new StockValueParserAsyncTask(mainActivity, stock).execute();
                        if (loadFailed) {
                            // Remove bugged symbols
                            databaseHandler.deleteStock(stockArray[0]);
                            return;
                        }
                    }
                } else {
                    noInternetDialog();
                }
                swiper.setRefreshing(false);
            }
        });
        if (!isNetworkOn()) {
            noInternetDialog();
        } else {
            for (String[] stockArray : databaseHandler.loadStocks()) {
                Stock stock = new Stock(stockArray[0], stockArray[1], 0.0, 0.0, 0.0);
                new StockValueParserAsyncTask(this, stock).execute();
                if (loadFailed) {
                    // Remove bugged symbols
                    databaseHandler.deleteStock(stockArray[0]);
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock stock = stockList.get(pos);
        databaseHandler.addStock(stock);
        String url = stockURL + stock.getSymbol();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        deleteDialog(stockList.get(pos));
        return true;
    }

    public void deleteDialog(final Stock stock) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete Stock Symbol " + stock.getSymbol() + "?");
        builder.setTitle(getString(R.string.delete_confirm));
        Drawable icon = ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert);
        final float[] NEGATIVE = {
                -1.0f, 0, 0, 0, 255, // red
                0, -1.0f, 0, 0, 255, // green
                0, 0, -1.0f, 0, 255, // blue
                0, 0, 0, 1.0f, 0  // alpha
        };
        // Invert the icon color
        icon.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
        builder.setIcon(icon);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println(stock);
                databaseHandler.deleteStock(stock.getSymbol());
                stockList.remove(stock);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addStockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.stock_selection));
        builder.setMessage(getString(R.string.enter_stock_symbol));
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText editTestSymbol = (EditText) view.findViewById(R.id.textEditSymbol);
                new StockSymbolParserAsyncTask(mainActivity, editTestSymbol.getText().toString()).execute();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void noInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.no_connection_msg));
        builder.setTitle(getString(R.string.no_connection_title));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void duplicateDialog(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock symbol " + symbol + " is already displayed");
        builder.setTitle(getString(R.string.duplicate_stock));
        Drawable icon = ContextCompat.getDrawable(this, android.R.drawable.ic_dialog_alert);
        final float[] NEGATIVE = {
                -1.0f, 0, 0, 0, 255, // red
                0, -1.0f, 0, 0, 255, // green
                0, 0, -1.0f, 0, 255, // blue
                0, 0, 0, 1.0f, 0  // alpha
        };
        icon.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
        builder.setIcon(icon);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addNewStock(Stock stock) {
        if (!stockList.contains(stock)) {
            stockList.add(stock);
            Collections.sort(stockList);
            databaseHandler.addStock(stock);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void processNewStock(Stock stock) {
        if (!stockList.contains(stock)) {
            new StockValueParserAsyncTask(this, stock).execute();
        } else {
            duplicateDialog(stock.getSymbol());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                if (isNetworkOn()) {
                    addStockDialog();
                } else {
                    noInternetDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkOn() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void failLoad() {
        loadFailed = true;
    }
}
