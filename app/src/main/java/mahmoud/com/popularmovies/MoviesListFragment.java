package mahmoud.com.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import mahmoud.com.popularmovies.modules.MoviesModule;

public class MoviesListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MoviesListAdapter mlistAdapter;

    private ProgressDialog dialog;

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
        mlistAdapter = new MoviesListAdapter(getActivity(), null, null);
        mRecyclerView.setAdapter(mlistAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("LOADING");
        dialog.setMessage("please wait loading movies data ... ");

        new GetMoviesTask().execute();

        return rootView;
    }

    public void updateUI(){
        new GetMoviesTask().execute();
    }

    class GetMoviesTask extends AsyncTask<String,String,String>{

        private final String TAG = GetMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String type = PreferenceManager
                        .getDefaultSharedPreferences(getActivity())
                        .getString(Utilty.PREF_SORT_TYPE, null);
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority(Utilty.BASE_IMDB_URL)
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(type)
                        .appendQueryParameter("api_key", BuildConfig.API_KEY);

                Log.i(TAG, builder.build().toString());

                URL url = new URL(builder.build().toString());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\n');
                }

                rd.close();
                Log.i(TAG, "res : " + response.toString());
                return response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                Gson gson = new Gson();
                final MoviesModule moviesModule = gson.fromJson(s, MoviesModule.class);
                mlistAdapter = new MoviesListAdapter(getActivity(), new ArrayList(moviesModule.getResults()), new MoviesListAdapter.ItemClickListner() {
                    @Override
                    public void OnItemClick(View v, int position) {
                        Log.i(TAG, position + " is clicked");


                        Intent intent = new Intent(getActivity(), MovieDataActivity.class);
                        MoviesModule.Movie movie = moviesModule.getResults().get(position);
                        intent.putExtra(MovieDataActivity.Movie_EXTRA, (Serializable) movie);
                        startActivity(intent);
                    }
                });
                mRecyclerView.setAdapter(mlistAdapter);
                Log.i(TAG, String.valueOf(moviesModule.getResults().size()));
                dialog.dismiss();
            }else{
                dialog.dismiss();
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

            //TODO: do something when no internet connection
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Movies fragment", "switching");
        updateUI();
        return true;
    }

}
