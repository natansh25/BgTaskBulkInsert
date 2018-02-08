package com.example.natan.backgroundtasks;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.natan.backgroundtasks.Database.Contract;
import com.example.natan.backgroundtasks.Network.NetworkUtils;
import com.example.natan.backgroundtasks.Pojo.Contacts;
import com.example.natan.backgroundtasks.Pojo.FavAdapter;
import com.example.natan.backgroundtasks.Pojo.MyAdapter;
import com.example.natan.backgroundtasks.Utils.PrefrencesKeys;

import org.json.JSONException;


import java.net.URL;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private FavAdapter mFavAdapter;
    private ProgressBar mProgressBar;
    private static final int LOADER_ID = 0;


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
        URL ur1 = NetworkUtils.buildURl();
        new MyAsyncTask().execute(ur1);


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
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);


            }
        }).attachToRecyclerView(mRecyclerView);


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

        Log.i("hula", String.valueOf(data));

        mFavAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mFavAdapter.swapCursor(null);

    }


    //-------------------------------Async Task---------------------------------


    class MyAsyncTask extends AsyncTask<URL, Void, List<Contacts>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Contacts> doInBackground(URL... urls) {


            try {
                List<Contacts> json = NetworkUtils.fetchContactData(urls[0]);
                return json;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<Contacts> contacts) {
            super.onPostExecute(contacts);
            mProgressBar.setVisibility(View.INVISIBLE);

            mMyAdapter = new MyAdapter(contacts, new MyAdapter.RecyclerViewClickListener() {
                @Override
                public void onClick(Contacts contacts) {
                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
                    i.putExtra(PrefrencesKeys.Parcelable_key, contacts);
                    startActivity(i);
                }
            });

            mRecyclerView.setAdapter(mMyAdapter);
            mMyAdapter.notifyDataSetChanged();
        }
    }


    //--------------------------menu option-----------------------------


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_JSON) {
            mRecyclerView.setAdapter(mMyAdapter);


        }
        if (id == R.id.action_FAV) {

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Cursor> cursorLoader = loaderManager.getLoader(LOADER_ID);
            if (cursorLoader == null) {
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);

            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

            }

            mFavAdapter = new FavAdapter(this, new FavAdapter.RecyclerViewClickListenerFav() {
                @Override
                public void onClick(Contacts contacts) {
                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
                    i.putExtra(PrefrencesKeys.Parcelable_key, contacts);
                    startActivity(i);
                }
            });
            mRecyclerView.setAdapter(mFavAdapter);


        }
        return super.onOptionsItemSelected(item);
    }
}
