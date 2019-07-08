package com.example.newsapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentArticle extends Fragment{
    private String url;
    private Menu myMenu;

    private WebView webview;
    private ProgressBar spinner;
    String showHideInitialUse="show";
    FragmentArticle(String url,Menu myMenu){
        this.myMenu=myMenu;
        this.url=url;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.article_view_fragment,container,false);
        spinner=view.findViewById(R.id.loadingPage);
        webview=view.findViewById(R.id.article_new);


        myMenu.findItem(R.id.addButton).setVisible(true);


        webview.setWebViewClient(new CustomWebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Force links and redirects to open in the WebView instead of in a browser
        webview.loadUrl(url);
        return view;
    }
    private class CustomWebViewClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon){
            if(showHideInitialUse.equals("show")){
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url){
            showHideInitialUse="hide";
            spinner.setVisibility(View.GONE);
            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view,url);
        }
    }

}