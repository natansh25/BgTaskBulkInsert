package com.example.natan.backgroundtasks.BackGroundTasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

/**
 * Created by natan on 2/7/2018.
 */

public class FireBaseJobService extends JobService {
    // implementing AsyncTask so it runs on the background thread and doesnt disturb the main thread while running

    private AsyncTask<Void, Void, Void> mData;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {


        mData = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Context context = getApplicationContext();
                Notif.notify(context);
                jobFinished(params, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(params, false);
            }
        };

        mData.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mData != null) {
            mData.cancel(true);
        }
        return true;
    }
}
