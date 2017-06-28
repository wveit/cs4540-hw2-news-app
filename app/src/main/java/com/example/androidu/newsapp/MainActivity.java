package com.example.androidu.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText mSearchEditText;
    RecyclerView mResultsRecyclerView;
    TextView mErrorMessageTextView;
    ProgressBar mProgressBar;

    NewsItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = (EditText) findViewById(R.id.et_search_box);
        mResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_news_items);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mResultsRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new NewsItemRecyclerViewAdapter();
        mResultsRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.search_menu_item){
            search();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search(){
        // Right now, the search function is ignoring the contents
        // of the search terms in mSearchEditText. Later, the search terms
        // entered in mSearchEditText should be used during the search.

        String sourceString = "the-next-web";
        String sortByString = "latest";
        new NewsQueryTask().execute(sourceString, sortByString);

        Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show();
    }

    private void showErrorMessage(){
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mResultsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showSearchResults(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mResultsRecyclerView.setVisibility(View.VISIBLE);
    }

    class NewsQueryTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            if(params.length < 2)
                return null;

            String sourceString = params[0];
            String sortByString = params[1];

            URL url = NetworkUtils.buildUrl(sourceString, sortByString);

            Log.d("MainActivity", "Querying URL: " + url.toString());

            String result = null;
            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            }
            catch(IOException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgressBar.setVisibility(View.INVISIBLE);

            if(s == null){
                showErrorMessage();
            }
            else{
                showSearchResults();

                ArrayList<NewsItem> itemList = new ArrayList<>();
                boolean success = JsonUtils.parse(itemList, s);
                if(success){
                    mAdapter = new NewsItemRecyclerViewAdapter();
                    mAdapter.setNewsItemList(itemList);
                    mResultsRecyclerView.setAdapter(mAdapter);
                    System.out.println(s);
                    System.out.println("number news items: " + itemList.size());
                }

            }
        }
    }
}
