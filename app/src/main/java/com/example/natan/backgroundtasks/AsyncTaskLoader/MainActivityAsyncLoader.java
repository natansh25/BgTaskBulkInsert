package com.example.natan.backgroundtasks.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.natan.backgroundtasks.BackGroundTasks.MyService;
import com.example.natan.backgroundtasks.BackGroundTasks.Notif;
import com.example.natan.backgroundtasks.BackGroundTasks.SyncUtils;
import com.example.natan.backgroundtasks.Database.Contract;
import com.example.natan.backgroundtasks.DetailActivity;
import com.example.natan.backgroundtasks.MainActivity;
import com.example.natan.backgroundtasks.Network.NetworkUtils;
import com.example.natan.backgroundtasks.Pojo.Contacts;
import com.example.natan.backgroundtasks.Pojo.FavAdapter;
import com.example.natan.backgroundtasks.Pojo.MyAdapter;
import com.example.natan.backgroundtasks.R;
import com.example.natan.backgroundtasks.Utils.PrefrencesKeys;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

/**
 * Created by natan on 2/3/2018.
 */

public class MainActivityAsyncLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private FavAdapter mFavAdapter;
    private ProgressBar mProgressBar;
    private String URL_EXTRA = "nomac";

    //unikely identifiny the loader !!
    private static final int CONTACT_LOADER = 25;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);

        mRecyclerView.addItemDecoration(itemDecor);

        SyncUtils.initialize(this);

        getSupportLoaderManager().initLoader(CONTACT_LOADER,null,this);


        // setting up the adapter

        mFavAdapter = new FavAdapter(this, new FavAdapter.RecyclerViewClickListenerFav() {
            @Override
            public void onClick(Contacts contacts) {
                Intent i = new Intent(MainActivityAsyncLoader.this, DetailActivity.class);
                i.putExtra(PrefrencesKeys.Parcelable_key, contacts);
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(mFavAdapter);

    }


    //-------------menu options------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_NOTIF) {

            Notif.notify(this);


        }
        if (id == R.id.action_Service) {

            SyncUtils.startImmediateSync(this);


        }
        return super.onOptionsItemSelected(item);

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        return new AsyncTaskLoader<Cursor>(this) {


            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {


                return getContentResolver().query(Contract.Fav.CONTENT_URI, null, null, null, null);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mFavAdapter.swapCursor(null);

    }


}