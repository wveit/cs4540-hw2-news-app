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
            holder.bind(mNewsItemList.get(position));
        }
    }






    public class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        TextView mDateTextView;
        String mUrlString;

        public NewsItemViewHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.tv_news_item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_news_item_description);
            mDateTextView = (TextView) itemView.findViewById(R.id.tv_news_item_date);
        }

        public void bind(NewsItem newsItem){
            mUrlString = newsItem.getUrl();

            mTitleTextView.setText("Title: " + newsItem.getTitle());
            mDescriptionTextView.setText("Description: " + newsItem.getDescription());
            mDateTextView.setText("Date: " + newsItem.getDate());
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlString)));
        }
    }



}
