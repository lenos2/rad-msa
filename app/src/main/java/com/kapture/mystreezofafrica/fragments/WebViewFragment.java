package com.kapture.mystreezofafrica.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.kapture.mystreezofafrica.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {


    View v;
    WebView webView;
    String url = "http://www.apppages.appsareus.co.zw/mystreezofafrica/tours.php";
    public WebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        webView = (WebView)v.findViewById(R.id.webview);

        WebSettings  webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }
}
