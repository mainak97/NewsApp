package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView news_list;
    TextView logo;
    ArrayList<String> list=new ArrayList<>();
    String url="https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=bdf9851146d24ea497cf4397288f4cde";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        logo=findViewById(R.id.main_logo_tv);
        logo.setTextColor(getResources().getColor(R.color.logo1,getResources().newTheme()));
        news_list=findViewById(R.id.main_list_rv);
        final Adapter ad=new Adapter(this,list);
        news_list.setLayoutManager(new LinearLayoutManager(this));
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
                    Log.i("Mainak",a.getJSONObject(0).getString("title").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<a.length();i++){
                    try {
                        list.add(a.getJSONObject(i).getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ad.addnews(list);
                news_list.setAdapter(ad);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Mainak", "onErrorResponse: "+error.getMessage());
            }
        });
        Log.i("Mainak","list="+list);
        requestQueue.add(request);
    }
}
