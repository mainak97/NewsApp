package com.example.newsapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<News> news;
    public Adapter(Context context, ArrayList<News> response){
        mContext= context;
        news=response;
    }
    public void addNews(ArrayList<News> a){
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.test.setText(news.get(position).getTitle());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x=new Intent(mContext,ArticleView.class).putExtra("news",news.get(position));
                mContext.startActivity(x);
            }
        });
        Picasso.get().load(news.get(position).getImage_url()).fit().into(holder.news_card);
    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView test;
        private ImageView news_card;
        private CardView card_view;
        public MyViewHolder(@NonNull View view) {
            super(view);
            test=view.findViewById(R.id.test);
            news_card=view.findViewById(R.id.news_card);
            card_view=view.findViewById(R.id.card);
        }
    }
}