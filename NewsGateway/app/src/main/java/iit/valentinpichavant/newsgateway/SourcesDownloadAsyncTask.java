package iit.valentinpichavant.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by valentinpichavant on 4/15/17.
 */

public class SourcesDownloadAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String SOURCE_SEARCH_HTTP = "https://newsapi.org/v1/sources?language=en&country=us";
    private static final String SOURCE_SEARCH_CATEGORY = "&category=";
    private static final String SOURCE_SEARCH_API = "&apiKey=fc71d06a7b2a4737be892816b7281f3c";
    private static final String TAG = "AsyncSourcesLoader";
    private String category;
    private NewsGatewayService newsGatewayService;

    public SourcesDownloadAsyncTask(NewsGatewayService newsGatewayService, String category) {
        this.newsGatewayService = newsGatewayService;
        this.category = category;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        newsGatewayService.setSourcesList(parseJSON(s));
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString;
        StringBuilder sb = new StringBuilder();
        if (category != null) {
            urlString = SOURCE_SEARCH_HTTP + SOURCE_SEARCH_CATEGORY + this.category + SOURCE_SEARCH_API;
        } else {
            urlString = SOURCE_SEARCH_HTTP + SOURCE_SEARCH_API;
        }
        Uri dataUri = Uri.parse(urlString);
        String urlToUse = dataUri.toString().replaceAll(" ", "%20");
        Log.d(TAG, "doInBackground: " + urlToUse);
        try {
            URL url = new URL(urlToUse);
            URLConnection conn = url.openConnection();
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
        Log.d(TAG, "Sources : " + sb.toString());
        return sb.toString();
    }

    private ArrayList<Source> parseJSON(String s) {
        ArrayList<Source> sourcesList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArrayArticles = jObjMain.getJSONArray("sources");
            for (int i = 0; i < jArrayArticles.length(); i++) {
                JSONObject jsonObject = (JSONObject) jArrayArticles.get(i);
                sourcesList.add(new Source(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("url"), jsonObject.getString("category")));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
        }
        return sourcesList;
    }
}
