package com.example.newsapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private String color[]={"#F44336","#4CAF50","#2196F3"};
    private Context mContext;
    private ArrayList<String> news;
    public Adapter(Context context, ArrayList<String> response){
        mContext= context;
        news=response;
        //Log.i("Mainak", "Adapter: "+news.toString());
    }
    public void addnews(ArrayList<String> a){
        news.addAll(a);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.test.setText(news.get(position));
    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView test;
        public MyViewHolder(@NonNull View view) {
            super(view);
            test=view.findViewById(R.id.test);
        }
    }
}