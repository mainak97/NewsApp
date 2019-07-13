package com.example.newsapp;
import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.realm.RealmResults;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context mContext;
    private RealmResults<News> news;
    private FragmentManager fm;
    private ActionBar actionBar;
    private SwipeRefreshLayout mSwipeRefresh;
    private Menu myMenu;
    public Adapter(Context context, RealmResults<News> response, FragmentManager fm, Menu myMenu, ActionBar actionBar, SwipeRefreshLayout mSwipeRefresh){
        mContext= context;
        news=response;
        this.mSwipeRefresh=mSwipeRefresh;
        this.actionBar=actionBar;
        this.myMenu=myMenu;
        this.fm=fm;
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

        //Converting the published date to user Readable date/time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date=null;
        try {
            date = sdf.parse(news.get(position).getPublishDate());
        } catch (ParseException e) {
            Log.d("DEBJOY",e.getMessage());
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd", Locale.ENGLISH);
        outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        if(outputFormat.format(new Date()).equals(outputFormat.format(date))){
            outputFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            holder.publishDate.setText(outputFormat.format(date));
        }else{
            outputFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
            outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateValue[]=outputFormat.format(date).split(" ",0);
            holder.publishDate.setText(dateValue[0]+" "+dateValue[1]+"'"+dateValue[2]);
        }

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.headlines_list,new FragmentArticle(news.get(position),myMenu,actionBar,mSwipeRefresh)).addToBackStack("tag");
                ft.commit();
            }
        });
        Picasso.get()
                .load(news.get(position).getImage_url())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.imageload)
                .into(holder.news_card);
    }
    @Override
    public int getItemCount() {
        return news.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView test;
        private ImageView news_card;
        private CardView card_view;
        private TextView publishDate;
        public MyViewHolder(@NonNull View view) {
            super(view);
            test=view.findViewById(R.id.test);
            publishDate=view.findViewById(R.id.dateShow);
            news_card=view.findViewById(R.id.news_card);
            card_view=view.findViewById(R.id.card);
        }
    }
}