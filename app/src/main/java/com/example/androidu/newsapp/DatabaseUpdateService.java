/*

    This class allows the complex task of updating the apps database from newsapi.org web api to
        be run. This action can either be performed once, or on a recurring basis.

    updateOnce(...) causes the database to be updated from the newsapi.org api... one time. This
        action is performed in a separate thread using AsyncTask. When the task is complete, it
        broadcasts an intent, so that other parts of the app can perform actions after a database
        update (such as updating the ui)

    startRecurringUpdates(...) causes the database to be updated repeatedly. This should occur once
        every minute. FireBaseJobDispatcher is used to start this service at the correct times. This
        method will cause an intent to be broadcast each time it runs.

    stopRecurringUpdates(...) causes the reccurring job to stop so that the database will no longer
        be updated every minute.

 */

package com.example.androidu.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.example.androidu.newsapp.database.MyDatabase;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;


public class DatabaseUpdateService extends JobService {
    private static final String LOG_TAG = "DatabaseUpdateService";
    private static final String RECURRING_JOB_TAG = "database-update-recurring";
    public static final String DATABASE_UPDATED_BROADCAST_ACTION = "com.example.androidu.newsapp.DATABASE_UPDATED";

    @Override
    public boolean onStartJob(JobParameters job) {
        updateOnce(this);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public static void updateOnce(final Context context){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MyDatabase db = new MyDatabase(context);
                db.open();
                Tasks.syncDatabase(db);
                db.close();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                sendBroadcastIntent(context);
            }
        };

        task.execute();
    }

    public static void startRecurringUpdates(Context context){

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job myJob = dispatcher.newJobBuilder()
                .setService(DatabaseUpdateService.class)
                .setTag(RECURRING_JOB_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(5, 6))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();

        dispatcher.schedule(myJob);
    }

    public static void stopRecurringUpdates(Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        dispatcher.cancel(RECURRING_JOB_TAG);
    }

    public static void sendBroadcastIntent(Context context){
        Intent intent = new Intent();
        intent.setAction(DATABASE_UPDATED_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }



}
