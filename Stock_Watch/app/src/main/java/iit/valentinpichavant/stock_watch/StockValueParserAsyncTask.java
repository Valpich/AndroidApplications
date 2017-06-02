package iit.valentinpichavant.stock_watch;

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

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class StockValueParserAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String STOCK_SEARCH_HTTP = "http://finance.google.com/finance/info?client=ig&q=";
    private static final String STOCK_SEARCH_QUERY = STOCK_SEARCH_HTTP;
    private static final String TAG = "AsyncStockValueLoader";
    private MainActivity mainActivity;
    private Stock stock;

    public StockValueParserAsyncTask(MainActivity ma, Stock stock) {
        mainActivity = ma;
        this.stock = stock;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        boolean success = parseJSON(stock, s);
        Log.d(TAG, "updatedStock: " + stock);
        if (!success) {
            symbolNotFoundDialog(stock.getSymbol());
            mainActivity.failLoad();
        } else {
            mainActivity.addNewStock(stock);
        }
    }

    public void symbolNotFoundDialog(String symbol) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(mainActivity.getString(R.string.symbol_not_found_msg));
        builder.setTitle(mainActivity.getString(R.string.symbol_not_found_title) + symbol);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(STOCK_SEARCH_QUERY + stock.getSymbol());
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


    private boolean parseJSON(Stock stock, String s) {
        boolean success = false;
        try {
            JSONArray jObjMain = new JSONArray(s.substring(3, s.length()));
            JSONObject jStock = (JSONObject) jObjMain.get(0);
            Double lastTradePrice = jStock.getDouble("l");
            stock.setLastTradePrice(lastTradePrice);
            Double priceChangeAmmount = jStock.getDouble("c");
            stock.setPriceChangeAmmount(priceChangeAmmount);
            Double priceChangePercentage = jStock.getDouble("cp");
            stock.setPriceChangePercentage(priceChangePercentage);
            success = true;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

}
