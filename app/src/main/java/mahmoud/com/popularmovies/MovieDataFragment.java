package mahmoud.com.popularmovies;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.text.TextDirectionHeuristicCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mahmoud.com.popularmovies.data.MoviesContract;
import mahmoud.com.popularmovies.modules.MoviesModule;
import mahmoud.com.popularmovies.modules.Reviews;
import mahmoud.com.popularmovies.modules.Videos;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDataFragment extends Fragment {


    private static final String TAG = MovieDataFragment.class.getSimpleName();

    private TextView titleTextView;
    private TextView dateTextView;
    private TextView ratingTextView;
    private ImageView posterImageView;
    private TextView descTextView;
    private TextView favouriteButton;
    private Cursor mCursor;

    private ListView videosListView ;


    private ArrayAdapter<String> videosAdapter;

    private String mType = null;
    private long _id;
    private String movieId = null;

    private int LOADER_ID = 5;
    private TextView vedioTextView;
    private TextView reviewTExtView;
    private LinearLayout linearLayout;

    public MovieDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_data, container, false);
        Bundle args = getArguments();


        mType = args.getString(Utilty.DATA_FRAGMENT_BUNDLE_SORT_TYPE);
        _id = args.getLong(Utilty.DATA_FRAGMENT_BUNDLE__ID);
        movieId = args.getString(Utilty.DATA_FRAGMENT_BUNDLE_MOVIE_id);
        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);

        titleTextView = (TextView) rootView.findViewById(R.id.movie_data_title);
        dateTextView = (TextView) rootView.findViewById(R.id.movie_data_date);
        ratingTextView = (TextView) rootView.findViewById(R.id.movie_data_rating);
        posterImageView = (ImageView) rootView.findViewById(R.id.movie_data_poster);
        descTextView = (TextView) rootView.findViewById(R.id.movie_data_desc_textView);
        videosListView = (ListView) rootView.findViewById(R.id.trailers_list_view);
        vedioTextView = (TextView) rootView.findViewById(R.id.detail_video_text_view);
        reviewTExtView = (TextView) rootView.findViewById(R.id.reviews_text_view);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.list_view_replacement);
        favouriteButton = (Button) rootView.findViewById(R.id.favourite_button);

        videosAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.vedio_list_item,
                new ArrayList<String>()
        );
        videosListView.setAdapter(videosAdapter);

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Cursor cursor = getContext().getContentResolver().query(MoviesContract.FavouriteTable.CONTENT_URI,
                        null, null,
                        null, null);
                boolean in = false;
                int id = 0;
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String movieid = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_ID));
                    Log.i(TAG, movieid);
                    Log.i(TAG, movieId);
                    id = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavouriteTable._ID));
                    if (movieid.equals(movieId)){
                        Log.i(TAG, "equal");
                        in = true;
                        break;
                    }
                    cursor.moveToNext();
                }
                if(!in)
                    favouriteButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorAccent));
                else
                    favouriteButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));
                if(in){
                    getContext().getContentResolver().delete(MoviesContract.FavouriteTable.buildFavouriteUri(id),
                            null, null);
                }else{
                    String title = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_TITLE));
                    String overview = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_OVERVIEW));
                    String posterPath = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_POSTER_PATH));
                    String voteAverage = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_VOTE));
                    String releaseDate = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_RELEASE_DATE));
                    String id2 = mCursor.getString(mCursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_ID));

                    ContentValues values = new ContentValues();
                    values.put(MoviesContract.PopularTable.TITLE, title);
                    values.put(MoviesContract.PopularTable.OVERVIEW, overview);
                    values.put(MoviesContract.PopularTable.POSTER_PATH, posterPath);
                    values.put(MoviesContract.PopularTable.VOTE, voteAverage);
                    values.put(MoviesContract.PopularTable.RELEASE_DATE, releaseDate);
                    values.put(MoviesContract.PopularTable.ID, String.valueOf(id2));
                    getContext().getContentResolver().insert(MoviesContract.FavouriteTable.CONTENT_URI, values);
                }
            }
        });

        return rootView;
    }
    LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, String.valueOf(_id));
            if (mType != null){
                if (mType.equals(Utilty.POPULAR)){
                    return new CursorLoader(getContext(),
                            MoviesContract.PopularTable.buildPopularUri(_id),
                            null,
                            null,
                            null,null);
                }else if(mType.equals(Utilty.TOP_RATED)){
                    return new CursorLoader(getContext(),
                            MoviesContract.TopRatedTable.buildTopRatedUri(_id),
                            null,
                            null,
                            null,null);
                }else if (mType.equals(Utilty.FAVOURITE)){
                    return new CursorLoader(getContext(),
                            MoviesContract.FavouriteTable.buildFavouriteUri(_id),
                            null,
                            null,
                            null,null);
                }
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            mCursor = data;
            if (data.getCount() > 0){
                String title = data.getString(data.getColumnIndex(MoviesContract.PopularTable.TITLE));
                String date = data.getString(data.getColumnIndex(MoviesContract.PopularTable.RELEASE_DATE));
                String rating = data.getString(data.getColumnIndex(MoviesContract.PopularTable.VOTE));;
                String posterPath = data.getString(data.getColumnIndex(MoviesContract.PopularTable.POSTER_PATH));;
                String desc = data.getString(data.getColumnIndex(MoviesContract.PopularTable.OVERVIEW));

                descTextView.setText(desc);
                titleTextView.setText(title);
                dateTextView.setText(date);
                ratingTextView.setText(rating);
                Picasso.with(getActivity()).load(Utilty.BASE_IMDB_IMG_URL + posterPath).into(posterImageView);
                new ReviewsTask().execute();
                new VideosTask().execute();

                Cursor cursor = getContext().getContentResolver().query(MoviesContract.FavouriteTable.CONTENT_URI,
                        null, null,
                        null, null);
                boolean in = false;
                int id = 0;
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String movieid = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteTable.COLUMN_ID));
                    id = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavouriteTable._ID));
                    if (movieid.equals(movieId)){
                        in = true;
                        break;
                    }
                    cursor.moveToNext();
                }
                if(in)
                    favouriteButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorAccent));
                else
                    favouriteButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public class VideosTask extends AsyncTask<String, Void, Videos> {

        Videos ret = null;

        @Override
        protected Videos doInBackground(String... params) {

            String LOG_TAG = ReviewsTask.class.getSimpleName();


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultStr = null;
            try {


                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority(Utilty.BASE_IMDB_URL)
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(movieId)
                        .appendPath("videos")
                        .appendQueryParameter("api_key", BuildConfig.API_KEY);
                URL url = new URL(builder.build().toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                resultStr = buffer.toString();
                Log.d(LOG_TAG,"Json input : " + resultStr);
                Videos videos ;
                Gson gson =  new Gson();
                videos =  gson.fromJson(resultStr, Videos.class);
                ret = videos;
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return ret;
        }


        @Override
        protected void onPostExecute(final Videos videos) {
            super.onPostExecute(videos);
            if (videos != null && videos.getResults().size() > 0){
                vedioTextView.setVisibility(View.VISIBLE);
                final List<Videos.ResultsBean> res = videos.getResults();
                ArrayList<String> vediosNames = new ArrayList<>();
                vediosNames.clear();
                for (int i = 0; i <res.size() ; i++) {
                    vediosNames.add(res.get(i).getName());
                }
                videosAdapter.clear();
                videosAdapter.addAll(vediosNames);
                videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                                res.get(position).getKey())));
                    }
                });
            }
        }
    }

    public class ReviewsTask extends AsyncTask<String, Void, Reviews> {
        Reviews ret = null;
        @Override
        protected Reviews doInBackground(String... params) {

            String LOG_TAG = ReviewsTask.class.getSimpleName();


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultStr = null;
            try {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority(Utilty.BASE_IMDB_URL)
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(movieId)
                        .appendPath("reviews")
                        .appendQueryParameter("api_key", BuildConfig.API_KEY);

                URL url = new URL(builder.build().toString());


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                resultStr = buffer.toString();
                Log.d(LOG_TAG, "Json input : " + resultStr);
                Reviews reviews = new Reviews();
                Gson gson = new Gson();
                reviews = gson.fromJson(resultStr, Reviews.class);
                ret = reviews;
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }


            return ret;
        }

        @Override
        protected void onPostExecute(final Reviews reviews) {
            super.onPostExecute(reviews);
            if (reviews != null){
                if((reviews.getResults().size() > 0)){
                    reviewTExtView.setVisibility(View.VISIBLE);
                    reviewsAdapter ad = new reviewsAdapter(getActivity(),R.layout.review_list_item, reviews.getResults());
                    for (int i = 0; i < ad.getCount() ; i++) {
                        View v = ad.getView(i, null, linearLayout);
                        linearLayout.addView(v);
                    }

                }
            }
        }
    }
}
