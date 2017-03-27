package mahmoud.com.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import mahmoud.com.popularmovies.BuildConfig;
import mahmoud.com.popularmovies.R;
import mahmoud.com.popularmovies.Utilty;
import mahmoud.com.popularmovies.data.MoviesContract;
import mahmoud.com.popularmovies.modules.MoviesModule;

/**
 * Created by mahmoud on 3/25/2017.
 */
public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = MoviesSyncAdapter.class.getSimpleName();
    private Context mContext;
    private ContentResolver mContentResolver;

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public MoviesSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {

        Log.i(TAG, "performing sync");
        Uri.Builder popularBuilder = new Uri.Builder();
        popularBuilder.scheme("https")
                .authority(Utilty.BASE_IMDB_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Utilty.POPULAR)
                .appendQueryParameter("api_key", BuildConfig.API_KEY);

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = popularBuilder.build().toString();

        StringRequest popularStringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        MoviesModule moviesModule = gson.fromJson(response, MoviesModule.class);
                        if(moviesModule != null && moviesModule.getResults()!= null){
                            List movies = moviesModule.getResults();
                            for (int i = 0; i < movies.size(); i++) {
                                MoviesModule.Movie movie = (MoviesModule.Movie) movies.get(i);
                                ContentValues values = new ContentValues();
//                                values.put();
                            }
                        }
                        // TODO: insert result into the DB

                        Log.i(TAG, "data loaded");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        requestQueue.add(popularStringRequest);

        Uri.Builder topRateBuilder = new Uri.Builder();
        topRateBuilder.scheme("https")
                .authority(Utilty.BASE_IMDB_URL)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Utilty.TOP_RATED)
                .appendQueryParameter("api_key", BuildConfig.API_KEY);

        RequestQueue topRateRequestQueue = Volley.newRequestQueue(mContext);
        String topRatedUrl = topRateBuilder.build().toString();
        StringRequest toprateStringRequest = new StringRequest(Request.Method.GET, topRatedUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        MoviesModule moviesModule = gson.fromJson(response, MoviesModule.class);

                        // TODO: insert result into the DB

                        Log.i(TAG, "data loaded");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        topRateRequestQueue.add(toprateStringRequest);


    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = Utilty.CONTENT_AUTHORITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                Utilty.CONTENT_AUTHORITY, bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), Utilty.CONTENT_AUTHORITY);

        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        MoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, Utilty.CONTENT_AUTHORITY, true);
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
















