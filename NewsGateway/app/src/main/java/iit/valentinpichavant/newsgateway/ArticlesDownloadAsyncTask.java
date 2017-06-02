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

public class ArticlesDownloadAsyncTask extends AsyncTask<String, Integer, String> {

    private static final String ARTICLES_SEARCH_HTTP = "https://newsapi.org/v1/articles?source=";
    private static final String ARTICLES_SEARCH_API = "&apiKey=fc71d06a7b2a4737be892816b7281f3c";
    private static final String TAG = "AsyncArticlesLoader";
    private String source;
    private NewsGatewayService newsGatewayService;

    public ArticlesDownloadAsyncTask(NewsGatewayService newsGatewayService, String source) {
        this.newsGatewayService = newsGatewayService;
        this.source = source;
    }

    @Override
    protected void onPreExecute() {
    }


    @Override
    protected void onPostExecute(String s) {
        newsGatewayService.setArticlesList(parseJSON(s));
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString;
        StringBuilder sb = new StringBuilder();
        if (source != null) {
            urlString = ARTICLES_SEARCH_HTTP + this.source + ARTICLES_SEARCH_API;
            Uri dataUri = Uri.parse(urlString);
            String urlToUse = dataUri.toString().replaceAll(" ", "%20").toLowerCase();
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
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
        }
        Log.d(TAG, "Articles : " + sb.toString());
        return sb.toString();
    }

    private ArrayList<Article> parseJSON(String s) {
        ArrayList<Article> articlesList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONArray jArrayArticles = jObjMain.getJSONArray("articles");
            for (int i = 0; i < jArrayArticles.length(); i++) {
                JSONObject jsonObject = (JSONObject) jArrayArticles.get(i);
                articlesList.add(new Article(jsonObject.getString("author"), jsonObject.getString("title"), jsonObject.getString("description"), jsonObject.getString("url"), jsonObject.getString("urlToImage"), jsonObject.getString("publishedAt")));
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
        }
        return articlesList;
    }
}
