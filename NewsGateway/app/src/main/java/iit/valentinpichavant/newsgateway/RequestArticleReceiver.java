package iit.valentinpichavant.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by valentinpichavant on 4/17/17.
 */

public class RequestArticleReceiver extends BroadcastReceiver {

    public static final String BROADCAST_ARTICLES = "BROADCAST_ARTICLES";
    public static final String DATA_ARTICLES = "DATA_ARTICLES";
    private MainActivity ma;

    public RequestArticleReceiver(MainActivity ma) {
        this.ma = ma;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BROADCAST_ARTICLES:
                ArrayList<Article> value;
                if (intent.hasExtra(DATA_ARTICLES)) {
                    value = (ArrayList<Article>) intent.getSerializableExtra(DATA_ARTICLES);
                    System.out.println(value);
                    ma.setArticlesList(value);
                }
                break;
            default:
                break;
        }
    }
}
