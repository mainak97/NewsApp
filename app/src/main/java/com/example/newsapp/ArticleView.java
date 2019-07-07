package com.example.newsapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ArticleView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.article_view);
        News n= (News) getIntent().getSerializableExtra("news");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.details_container,new Fragment_Article(n.getArticle_url()));
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuitem){
        Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
