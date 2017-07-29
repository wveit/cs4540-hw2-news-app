package com.example.androidu.newsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.androidu.newsapp.database.MyDatabase;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = "MainActivity";
    private static final int LOADER_ID = 34;

    private RecyclerView mResultsRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;

    private MyDatabase mDatabase;
    private NewsItemRecyclerViewAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultsRecyclerView = (RecyclerView) findViewById(R.id.rv_news_items);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_indicator);

        mDatabase = new MyDatabase(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mResultsRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewsItemRecyclerViewAdapter(mDatabase);
        mResultsRecyclerView.setAdapter(mAdapter);

        updateView();

        DatabaseUpdateService.startRecurringUpdates(this);

        // Check if app has been run before. If not, populate the database and mark app as
        // having been run.
        if(!Tasks.appIsInitialized(this)){
            DatabaseUpdateService.updateOnce(this);
            Tasks.markAppInitialized(this);
        }
    }


    @Override
    protected void onStart() {
        mDatabase.open();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mProgressBar.setVisibility(View.INVISIBLE);
        IntentFilter intentFilter = new IntentFilter(DatabaseUpdateService.DATABASE_UPDATED_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mDatabase.close();
        super.onStop();
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
            updateView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void updateView(){
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(LOADER_ID, new Bundle(), this);
        Log.d(TAG, "updateView()");
    }





    private void showProgressIndicator(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mResultsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showSearchResults(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mResultsRecyclerView.setVisibility(View.VISIBLE);
    }

    //////////////////////////////////////////
    //
    //  Broadcast Receiver
    //
    //////////////////////////////////////////

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

    //////////////////////////////////////////
    //
    //  AsyncTaskLoader Callbacks
    //
    //////////////////////////////////////////


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                showProgressIndicator();
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                mDatabase.selectAll();
                return null;
            }


        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        Log.d(TAG, "onLoadFinished()");
        if(mDatabase.getNumCursorRows() < 1){
            showErrorMessage();
        }
        else{
            Log.d(TAG, "updating recyclerView, numItems: " + mDatabase.getNumCursorRows());
            mAdapter = new NewsItemRecyclerViewAdapter(mDatabase);
            mResultsRecyclerView.setAdapter(mAdapter);
            showSearchResults();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


}
