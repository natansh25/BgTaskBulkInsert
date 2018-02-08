package com.example.natan.backgroundtasks.AsyncTaskLoader;

        import android.annotation.SuppressLint;
        import android.content.Intent;
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
        import com.example.natan.backgroundtasks.DetailActivity;
        import com.example.natan.backgroundtasks.MainActivity;
        import com.example.natan.backgroundtasks.Network.NetworkUtils;
        import com.example.natan.backgroundtasks.Pojo.Contacts;
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

public class MainActivityAsyncLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Contacts>> {

    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
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
        URL ur11 = NetworkUtils.buildURl();

        Bundle bundle = new Bundle();
        bundle.putString(URL_EXTRA, ur11.toString());

        getSupportLoaderManager().initLoader(CONTACT_LOADER, bundle, this);

        SyncUtils.initialize(this);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Contacts>> onCreateLoader(int id, final Bundle args) {


        return new AsyncTaskLoader<List<Contacts>>(this) {


            // list variable for loading cached data
            List<Contacts> list;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);


                if (list != null) {
                    deliverResult(list);
                } else {
                    forceLoad();
                }


            }

            @Override
            public List<Contacts> loadInBackground() {
                String url = args.getString(URL_EXTRA);

                if (url == null) {
                    return null;
                }

                try {
                    URL url1 = new URL(url);
                    List<Contacts> json = NetworkUtils.fetchContactData(url1);
                    return json;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }


            }

            //Loader caching if any configuration changes occur so that the device doesnt reload and doesnt perform extra network calls


            @Override
            public void deliverResult(List<Contacts> data) {
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<List<Contacts>> loader, List<Contacts> data) {


        mProgressBar.setVisibility(View.INVISIBLE);

        if (data == null) {

            Toast.makeText(this, "No data Sorry !!", Toast.LENGTH_SHORT).show();

        } else {
            mMyAdapter = new MyAdapter(data, new MyAdapter.RecyclerViewClickListener() {
                @Override
                public void onClick(Contacts contacts) {
                    Intent i = new Intent(MainActivityAsyncLoader.this, DetailActivity.class);
                    i.putExtra(PrefrencesKeys.Parcelable_key, contacts);
                    startActivity(i);
                }
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mMyAdapter);
            mMyAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void onLoaderReset(Loader<List<Contacts>> loader) {

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

}