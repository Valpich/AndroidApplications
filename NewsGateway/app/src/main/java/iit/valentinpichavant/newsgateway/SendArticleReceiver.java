package iit.valentinpichavant.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by valentinpichavant on 4/17/17.
 */

public class SendArticleReceiver extends BroadcastReceiver {

    public static final String BROADCAST_ARTICLES = "BROADCAST_ARTICLES";
    private NewsGatewayService newsGatewayService;

    public SendArticleReceiver(NewsGatewayService newsGatewayService) {
        this.newsGatewayService = newsGatewayService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case BROADCAST_ARTICLES:
                newsGatewayService.sendArticleList(intent.getStringExtra("source"));
                break;
            default:
                break;
        }
    }
}
