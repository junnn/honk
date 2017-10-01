package bloop.honk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bloop.honk.R;

/**
 * Created by Don on 2017/10/01.
 */

public class NewsDetails extends AppCompatActivity{
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_news_details);
        webView =(WebView) findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(bundle.getString("Link"));
        webView.setWebViewClient(new WebViewClient());
    }
}
