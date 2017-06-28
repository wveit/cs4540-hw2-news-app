package com.example.androidu.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsItemRecyclerViewAdapter extends RecyclerView.Adapter<NewsItemRecyclerViewAdapter.NewsItemViewHolder> {

    private ArrayList<NewsItem> mNewsItemList = null;

    public void setNewsItemList(ArrayList<NewsItem> newsItemList){
        mNewsItemList = newsItemList;
    }

    @Override
    public int getItemCount() {
        if(mNewsItemList == null)
            return 0;
        else
            return mNewsItemList.size();
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(R.layout.news_item_layout, parent, false);
        NewsItemViewHolder viewHolder = new NewsItemViewHolder(view);
        view.setOnClickListener(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder holder, int position) {
        if(mNewsItemList != null){
            NewsItem item = mNewsItemList.get(position);
            holder.bind(item.getTitle(), item.getDescription(), item.getUrl());
        }
    }






    public class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        TextView mUrlTextView;
        String mUrlString;

        public NewsItemViewHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.tv_news_item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_news_item_description);
            mUrlTextView = (TextView) itemView.findViewById(R.id.tv_news_item_url);
        }

        public void bind(String title, String description, String url){
            mUrlString = url;


            mTitleTextView.setText("Title: " + title);
            mDescriptionTextView.setText("Description: " + description);
            mUrlTextView.setText("URL: " + url);
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlString)));
        }
    }



}
