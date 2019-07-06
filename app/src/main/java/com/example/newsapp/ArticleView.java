package com.example.newsapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleView extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.article_view);
        News n= (News) getIntent().getSerializableExtra("news");
        WebView t=findViewById(R.id.article);
        t.loadUrl(n.getArticle_url());
        t.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
