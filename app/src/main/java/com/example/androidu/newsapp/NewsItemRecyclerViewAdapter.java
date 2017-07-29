package com.example.androidu.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidu.newsapp.database.MyDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsItemRecyclerViewAdapter extends RecyclerView.Adapter<NewsItemRecyclerViewAdapter.NewsItemViewHolder> {

    private ArrayList<NewsItem> mNewsItemList = null;
    private MyDatabase mDatabase;

    public NewsItemRecyclerViewAdapter(MyDatabase db){
        mDatabase = db;
    }


    @Override
    public int getItemCount() {
        return mDatabase.getNumCursorRows();
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
        mDatabase.moveCursorToRow(position);
        NewsItem item = new NewsItem(
                mDatabase.getAuthor(),
                mDatabase.getTitle(),
                mDatabase.getDescription(),
                mDatabase.getUrl(),
                mDatabase.getImageUrl(),
                mDatabase.getDate()
        );
        holder.bind(item);
    }






    public class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        TextView mDateTextView;
        String mUrlString;
        ImageView mImage;

        public NewsItemViewHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.tv_news_item_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_news_item_description);
            mDateTextView = (TextView) itemView.findViewById(R.id.tv_news_item_date);
            mImage = (ImageView) itemView.findViewById(R.id.iv_item_image);
        }

        public void bind(NewsItem newsItem){
            mUrlString = newsItem.getUrl();

            mTitleTextView.setText("Title: " + newsItem.getTitle());
            mDescriptionTextView.setText("Description: " + newsItem.getDescription());
            mDateTextView.setText("Date: " + newsItem.getDate());

            Picasso.with(itemView.getContext()).load(newsItem.getImageUrl()).into(mImage);
        }

        @Override
        public void onClick(View v) {
            v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlString)));
        }
    }



}
