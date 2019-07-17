package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
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
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FragmentArticle extends Fragment{
    private News news;
    private Menu myMenu;
    private ActionBar actionBar;
    private WebView webview;
    private ProgressBar spinner;
    private SwipeRefreshLayout mSwipeRefresh;
    String showHideInitialUse="show";
    FragmentArticle(News news, Menu myMenu, ActionBar actionBar, SwipeRefreshLayout mSwipeRefresh){
        this.myMenu=myMenu;
        this.mSwipeRefresh=mSwipeRefresh;
        this.actionBar=actionBar;
        this.news=news;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_view_fragment,container,false);
        spinner=view.findViewById(R.id.loadingPage);

        MenuItem searchViewItem = myMenu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        while(!searchView.isIconified()){searchView.setIconified(true);}

        webview=view.findViewById(R.id.article_new);
        webview.setVisibility(webview.INVISIBLE);
        //webview.setForceDarkAllowed(true);
        //--webview.set
        mSwipeRefresh.setEnabled(false);
        actionBar.setTitle("News Article");
        myMenu.findItem(R.id.addButton).setVisible(true);
        myMenu.findItem(R.id.location).setVisible(false);
        //Toast.makeText(getActivity(), "Article", Toast.LENGTH_SHORT).show();

        if(news.isSaved())
            myMenu.findItem(R.id.addButton).setIcon(R.drawable.ic_done_black_24dp);
        else
            myMenu.findItem(R.id.addButton).setIcon(R.drawable.add);
        webview.setWebViewClient(new CustomWebViewClient());
        myMenu.findItem(R.id.app_bar_search).setVisible(false);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Force links and redirects to open in the WebView instead of in a browser
        webview.loadUrl(news.getArticle_url());
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