package com.example.natan.backgroundtasks.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.natan.backgroundtasks.IdlingResource.SimpleIdlingResource;
import com.example.natan.backgroundtasks.MainActivity;
import com.example.natan.backgroundtasks.Network.NetworkUtils;
import com.example.natan.backgroundtasks.Pojo.Contacts;
import com.example.natan.backgroundtasks.Pojo.FavAdapter;
import com.example.natan.backgroundtasks.Pojo.MyAdapter;
import com.example.natan.backgroundtasks.R;
import com.example.natan.backgroundtasks.SettingsActivity;
import com.example.natan.backgroundtasks.Utils.PrefrencesKeys;
import com.facebook.stetho.Stetho;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

/**
 * Created by natan on 2/3/2018.
 */

public class MainActivityAsyncLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private FavAdapter mFavAdapter;
    private ProgressBar mProgressBar;
    private String URL_EXTRA = "nomac";
    private String value,edit_value;

    //unikely identifiny the loader !!
    public static final int CONTACT_LOADER = 25;




    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


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

        getSupportLoaderManager().restartLoader(CONTACT_LOADER, null, this);


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


        //swipe to delete

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                int id = (int) viewHolder.itemView.getTag();
                String sid = String.valueOf(id);
                Uri uri = Contract.Fav.CONTENT_URI;
                uri = uri.buildUpon().appendPath(sid).build();
                getContentResolver().delete(uri, null, null);
                getSupportLoaderManager().restartLoader(CONTACT_LOADER, null, MainActivityAsyncLoader.this);


            }
        }).attachToRecyclerView(mRecyclerView);

        Stetho.initializeWithDefaults(this);


        // Register the listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);



        //onSaveInstance





    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
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

            SystemClock.sleep(1000);
            Toast.makeText(this, "Service clicked !!", Toast.LENGTH_SHORT).show();
            SyncUtils.startImmediateSync(this);


        }
        if (id == R.id.action_setting) {

            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
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


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.list_pref_key))) {
            String key1 = getString(R.string.list_pref_key);
            String def = getString(R.string.pref_color_lable_light);

            value = sharedPreferences.getString(key1, def);

            Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

            if (value.equals("light")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            recreate();
        }
        if (key.equals("edit_text_pref"))
        {
            String key1 = getString(R.string.edit_pref_key);
            String def = getString(R.string.edit_pref_default);

            edit_value=sharedPreferences.getString(key1,def);

            setTitle(edit_value);




        }



    }



}