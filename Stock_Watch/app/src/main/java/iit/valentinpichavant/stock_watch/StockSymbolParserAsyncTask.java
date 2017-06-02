package iit.valentinpichavant.stock_watch;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class StockSymbolParserAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String STOCK_SEARCH_HTTP = "http://stocksearchapi.com/api/?api_key=";
    private static final String STOCK_SEARCH_API = "8f86390aceceed1438f215d3be4a16135b10d2c5";
    private static final String STOCK_SEARCH_PARAM = "&search_text=";
    private static final String STOCK_SEARCH_QUERY = STOCK_SEARCH_HTTP + STOCK_SEARCH_API + STOCK_SEARCH_PARAM;
    private static final String TAG = "AsyncStockSymbolLoader";
    private MainActivity mainActivity;
    private String userTyped;

    public StockSymbolParserAsyncTask(MainActivity ma, String userTyped) {
        this.mainActivity = ma;
        this.userTyped = userTyped;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        ArrayList<Stock> stockList = parseJSON(s);
        int stockListSize = stockList.size();
        if (stockListSize == 0) {
            symbolNotFoundDialog(userTyped);
        } else if (stockListSize == 1) {
            mainActivity.processNewStock(stockList.get(0));
        } else {
            multipleStockDialog(stockList);
        }
    }

    @Override
    protected String doInBackground(String... params) {


        Uri dataUri = Uri.parse(STOCK_SEARCH_QUERY + userTyped);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }


    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> stockList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String name = jStock.getString("company_name");
                String symbol = jStock.getString("company_symbol");
                stockList.add(new Stock(symbol, name, 0.0, 0.0, 0.0));
            }
            return stockList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return stockList;
    }

    public void symbolNotFoundDialog(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(mainActivity.getString(R.string.symbol_not_found_msg));
        builder.setTitle(mainActivity.getString(R.string.symbol_not_found_title) + symbol);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void multipleStockDialog(final ArrayList<Stock> stockList) {
        int size = stockList.size();
        final CharSequence[] sArray = new CharSequence[size];
        for (int i = 0; i < size; i++) {
            sArray[i] = stockList.get(i).getSymbol() + " - " + stockList.get(i).getCompany();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(mainActivity.getString(R.string.multiple_select_title));
        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.processNewStock(stockList.get(which));
            }
        });
        builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
