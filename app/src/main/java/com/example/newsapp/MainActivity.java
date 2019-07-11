package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Context mContext;
    private Menu myMenu;
    private int exitFlag=1;
    private ProgressBar loadingFirst;
    private TextView no_article;
    RealmResults<News> list;
    RealmResults<News> saved_list;
    RealmResults<News> current_list;
    FragmentManager fm = getSupportFragmentManager();
    Realm r;
    ActionBar actionBar;
    MenuItem headlinesDrawer;
    MenuItem savedDrawer;
    private FrameLayout headlineFrame;
    private SwipeRefreshLayout mSwipeRefresh;

    String url="https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=bdf9851146d24ea497cf4397288f4cde";
    @RequiresApi(api = M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        r=Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Headlines");
        headlineFrame=findViewById(R.id.headlines_list);
        headlineFrame.setBackgroundColor(Color.WHITE);

        loadingFirst=findViewById(R.id.loadingFirst);
        mContext=this;
        mDrawerLayout= findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        no_article=findViewById(R.id.no_articles);
        mSwipeRefresh=findViewById(R.id.swipeRefresh);
        //Toast.makeText(mContext, "Headlines", Toast.LENGTH_SHORT).show();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView=findViewById(R.id.nav_view);
        Menu menu1=navigationView.getMenu();
        headlinesDrawer=menu1.findItem(R.id.nav_headline);
        savedDrawer=menu1.findItem(R.id.nav_saved_list);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Log.i("Mainak", String.valueOf(menuItem.getItemId()));
                switch (menuItem.getItemId()){
                    case R.id.nav_headline:
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm,myMenu,actionBar,mSwipeRefresh),"HEADLINES").addToBackStack("HEADLINES");
                        actionBar.setTitle("Headlines");
                        headlineFrame.setBackgroundColor(Color.WHITE);
                        ft.commit();
                        mSwipeRefresh.setEnabled(true);
                        //Toast.makeText(mContext, "Headlines", Toast.LENGTH_SHORT).show();
                        current_list=list;
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        no_article.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.nav_saved_list:
                        saved_list=r.where(News.class).equalTo("saved",true).findAll().sort("timestamp",Sort.DESCENDING);
                        ft=fm.beginTransaction();
                        ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,saved_list,fm,myMenu,actionBar,mSwipeRefresh),"SAVED").addToBackStack("SAVED");
                        ft.commit();
                        mSwipeRefresh.setEnabled(false);
                       // Toast.makeText(mContext, "Saved Articles", Toast.LENGTH_SHORT).show();
                        headlineFrame.setBackgroundColor(Color.TRANSPARENT);
                        actionBar.setTitle("Saved Articles");
                        current_list=saved_list;
                        if(current_list.size()==0)
                            no_article.setVisibility(View.VISIBLE);
                        else
                            no_article.setVisibility(View.INVISIBLE);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_headline);
        loadData(1);
        mSwipeRefresh.setEnabled(true);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(0);
            }
        });

    }

    public void loadData(final int addReplace){
        if(!haveNetworkConnection()){
            exitDialog(addReplace);
            return;
        }
        Toast.makeText(mContext, "Fetching", Toast.LENGTH_SHORT).show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String extraUrl="&rand="+(int)(Math.random()*10000);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url+extraUrl,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray a = null;
                        loadingFirst.setVisibility(View.INVISIBLE);
                        Log.i("Mainak", "onResponse: "+response.toString());
                        try {
                            a=response.getJSONArray("articles");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Mainak",a.toString());
                        try {
                            Log.i("Mainak",a.getJSONObject(0).getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Realm r=Realm.getDefaultInstance();
                        for(int i=0;i<a.length();i++){
                            try {
                                JSONObject temp = a.getJSONObject(i);
                                r.beginTransaction();
                                r.copyToRealm(new News(temp.getString("title"),temp.getString("urlToImage"),temp.getString("url")));
                                r.commitTransaction();
                            }
                            catch (Exception e) {
                                r.cancelTransaction();
                                e.printStackTrace();
                            }
                        }
                        list=r.where(News.class).findAll().sort("timestamp", Sort.DESCENDING);
                        FragmentTransaction ft = fm.beginTransaction();
                        Log.i("mainak",String.valueOf(list.size()));
                        if(addReplace==1)
                        ft.add(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm,myMenu,actionBar,mSwipeRefresh));
                        else{
                            ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm,myMenu,actionBar,mSwipeRefresh),"HEADLINES");//.addToBackStack("HEADLINES");
                            Toast.makeText(mContext, "News updated", Toast.LENGTH_SHORT).show();
                            mSwipeRefresh.setRefreshing(false);
                        }

                        ft.commit();

                            current_list=list;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Mainak", "onErrorResponse: "+error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

        public void exitDialog(final int a){
            if(haveNetworkConnection()){
                loadData(a);
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Network Error!");
            builder.setMessage("Please connect to the internet!")
                    .setCancelable(false);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    exitDialog(a);
                }});

                    AlertDialog alert = builder.create();
            alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.addButton){
            //optionSelect();
            WebView w=findViewById(R.id.article_new);
            News temp=r.where(News.class).equalTo("article_url",w.getUrl()).findFirst();
            //Log.i("Mainak",w.getUrl());Log.i("Mainak",temp.toString());
            if(temp==null){
                Toast.makeText(this,"Cannot Save Youtube Video",Toast.LENGTH_SHORT).show();
                return true;
            }
            if(temp.isSaved()) {
                item.setIcon(R.drawable.add);
                r.beginTransaction();
                r.where(News.class).equalTo("article_url", w.getUrl()).findFirst().setSaved(false);
                r.commitTransaction();
                /*
                FragmentTransaction ft=fm.beginTransaction();
                getSupportFragmentManager().popBackStack();
                ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,current_list,fm,myMenu));
                ft.commit();*/


                if(current_list==list)
                    ((NavigationView)findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_headline);
                else
                    ((NavigationView)findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_saved_list);
                if(current_list.size()==0)
                    findViewById(R.id.no_articles).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.no_articles).setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Removed from saved articles",Toast.LENGTH_SHORT).show();
            }
            else{
                item.setIcon(R.drawable.ic_done_black_24dp);
                r.beginTransaction();
                r.where(News.class).equalTo("article_url", w.getUrl()).findFirst().setSaved(true);
                r.commitTransaction();
                no_article.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Added to saved articles",Toast.LENGTH_SHORT).show();
            }
        }
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item1,menu);

        myMenu=menu;
        myMenu.findItem(R.id.addButton).setVisible(false);

        return true;
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if(!myMenu.findItem(R.id.addButton).isVisible()){
            if(current_list==list){
                current_list=saved_list;
            }
            else{
                current_list=list;
            }
        }


        Log.i("Mainak",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        if(getSupportFragmentManager().getBackStackEntryCount()==0 && exitFlag==0)
            super.onBackPressed();
        else if(getSupportFragmentManager().getBackStackEntryCount()==0 && exitFlag==1){
            Toast.makeText(mContext, "Press back again to exit", Toast.LENGTH_SHORT).show();
            exitFlag=0;
            return;
        }else{
            exitFlag=1;
        }


        if(current_list!=null && current_list.size()==0)
            findViewById(R.id.no_articles).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.no_articles).setVisibility(View.INVISIBLE);


        myMenu.findItem(R.id.addButton).setVisible(false);


        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
           // Toast.makeText(mContext, "Headlines", Toast.LENGTH_SHORT).show();
            actionBar.setTitle("Headlines");
            headlinesDrawer.setChecked(true);
            headlineFrame.setBackgroundColor(Color.WHITE);
            return ;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1 ).getName();
        if(tag.equals("SAVED")){
            //Toast.makeText(mContext, "Saved Articles", Toast.LENGTH_SHORT).show();
            actionBar.setTitle("Saved Articles");
            headlineFrame.setBackgroundColor(Color.TRANSPARENT);
            mSwipeRefresh.setEnabled(false);
            savedDrawer.setChecked(true);
        }
        else if(tag.equals("HEADLINES"))
        {
            //Toast.makeText(mContext, "Headlines", Toast.LENGTH_SHORT).show();
            actionBar.setTitle("Headlines");
            headlineFrame.setBackgroundColor(Color.WHITE);
            mSwipeRefresh.setEnabled(true);
            headlinesDrawer.setChecked(true);
        }

    }


    @Override
    protected void onDestroy() {
        r.close();
        super.onDestroy();
    }
}
