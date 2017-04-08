package mahmoud.com.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import mahmoud.com.popularmovies.data.MoviesContract;
import mahmoud.com.popularmovies.modules.MoviesModule;

public class MoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MoviesListFragment.class.getSimpleName();
    private static final int LOADER_FLAG = 0;
    private RecyclerView mRecyclerView;
    private MoviesListAdapter mlistAdapter;

    private MoviesListAdapter.ItemClickListner listner;
    private int position;
    private static final String KEY_INSTANCE_STATE_RV_POSITION = "savedStateKeyinstance";
    private RecyclerView.LayoutManager mLayoutManager;


    public MoviesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_list_recyclerView);
        mlistAdapter = new MoviesListAdapter(getActivity(), null, new MoviesListAdapter.ItemClickListner() {
            @Override
            public void OnItemClick(View v, int position, String movieId, int _id) {
                listner.OnItemClick(v, position, movieId, _id);
            }

        });
        mRecyclerView.setAdapter(mlistAdapter);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(KEY_INSTANCE_STATE_RV_POSITION);
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("pos", position).commit();
        }
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getLayoutManager().scrollToPosition(position);

        getLoaderManager().initLoader(LOADER_FLAG, null,this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"resume " + String.valueOf(position));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "switching");
        getLoaderManager().restartLoader(LOADER_FLAG, null, this);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int i = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(KEY_INSTANCE_STATE_RV_POSITION, i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "creating loader");
        Log.i(TAG, String.valueOf(position));
        String type = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(Utilty.PREF_SORT_TYPE, Utilty.POPULAR);

        if (type.equals(Utilty.TOP_RATED)){
            Log.i(TAG, MoviesContract.TopRatedTable.CONTENT_URI.toString());
            return new CursorLoader(getContext(),
                    MoviesContract.TopRatedTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }else if(type.equals(Utilty.FAVOURITE)){
            Log.i(TAG, MoviesContract.FavouriteTable.CONTENT_URI.toString());
            return new CursorLoader(getContext(),
                    MoviesContract.FavouriteTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }else {
            Log.i(TAG, MoviesContract.PopularTable.CONTENT_URI.toString());
            return new CursorLoader(getContext(),
                    MoviesContract.PopularTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "content loaded");
        Log.i(TAG, String.valueOf(data.getCount()));
        mlistAdapter.swapCursor(data);
        mlistAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(mLayoutManager);
        position = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("pos", 0);
        Log.i(TAG, String.valueOf(position));
        mRecyclerView.getLayoutManager().scrollToPosition(position);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mlistAdapter.swapCursor(null);
    }

    public void setListner(MoviesListAdapter.ItemClickListner listner) {
        this.listner = listner;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }
}
