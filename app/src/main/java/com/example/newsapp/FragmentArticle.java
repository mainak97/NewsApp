package com.example.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentArticle extends Fragment{
    private String url;
    FragmentArticle(String url){
        this.url=url;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.article_view_fragment,container,false);
        WebView t=view.findViewById(R.id.article_new);
        t.loadUrl(url);
        //Toast.makeText(this.getContext(), "in framelayout", Toast.LENGTH_SHORT).show();
        /*t.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });*/
        WebSettings webSettings = t.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Force links and redirects to open in the WebView instead of in a browser
        t.setWebViewClient(new WebViewClient());
        return view;
    }
}