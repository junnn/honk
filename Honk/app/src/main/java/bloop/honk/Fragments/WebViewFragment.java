package bloop.honk.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bloop.honk.R;

/**
 * Created by Don on 21/10/2017.
 */

public class WebViewFragment extends Fragment {
    WebView webView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = view.findViewById(R.id.webview);
        Bundle bundle = this.getArguments();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(bundle.getString("Link"));

        webView.setWebViewClient(new WebViewClient());
        return view;
    }
}
