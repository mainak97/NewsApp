package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


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

import java.util.ArrayList;
import java.util.Objects;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Context mContext;
    ArrayList<News> list=new ArrayList<>();
    FragmentManager fm = getSupportFragmentManager();
    String url="https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=bdf9851146d24ea497cf4397288f4cde";
    @RequiresApi(api = M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        mDrawerLayout= findViewById(R.id.drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Log.i("Mainak", String.valueOf(menuItem.getItemId()));
                switch (menuItem.getItemId()){
                    case R.id.nav_headline:
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm)).addToBackStack("tag");
                        ft.commit();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.nav_saved_list:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm)).addToBackStack("tag");
                        ft.commit();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_headline);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray a = null;
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
                for(int i=0;i<a.length();i++){
                    try {
                        JSONObject temp=a.getJSONObject(i);
                        list.add(new News(temp.getString("title"),temp.getString("urlToImage"),temp.getString("url")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.headlines_list,new HeadlinesViewFragment(mContext,list,fm));
                ft.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Mainak", "onErrorResponse: "+error.getMessage());
            }
        });
        requestQueue.add(request);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);

        }
        else
            super.onBackPressed();
    }
    /*

    plus button on the right side of the appbar needs to be added dynamically some other way.
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
    }*/
}
