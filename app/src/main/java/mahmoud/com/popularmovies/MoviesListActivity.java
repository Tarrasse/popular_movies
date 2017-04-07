package mahmoud.com.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import mahmoud.com.popularmovies.sync.MoviesSyncAdapter;

public class MoviesListActivity extends AppCompatActivity {

    private static final String TAG = MoviesListActivity.class.getSimpleName();
    private boolean twoBane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MoviesSyncAdapter.initializeSyncAdapter(this);

        if(findViewById(R.id.movie_data_palce_holder) != null){
            twoBane = true;
        }else{
            twoBane = false;
        }

        MoviesListFragment fragment = new MoviesListFragment();
        fragment.setListner(new MoviesListAdapter.ItemClickListner() {
            @Override
            public void OnItemClick(View v, int position, String movieId, int _id) {
                String type = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext())
                        .getString(Utilty.PREF_SORT_TYPE, Utilty.POPULAR);
                if(twoBane){
                    Bundle args = new Bundle();
                    args.putString(Utilty.DATA_FRAGMENT_BUNDLE_SORT_TYPE, type);
                    args.putLong(Utilty.DATA_FRAGMENT_BUNDLE__ID,(long) _id);
                    args.putString(Utilty.DATA_FRAGMENT_BUNDLE_MOVIE_id,movieId);

                    MovieDataFragment fragment = new MovieDataFragment();
                    fragment.setArguments(args);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.movie_data_palce_holder, fragment)
                            .commit();

                    Log.i(TAG, "twoBane");
                }else{
                    Intent intent = new Intent(getBaseContext(), MovieDataActivity.class);


                    intent.putExtra(Utilty.DATA_FRAGMENT_BUNDLE_SORT_TYPE, type);
                    intent.putExtra(Utilty.DATA_FRAGMENT_BUNDLE__ID, (long) _id);
                    intent.putExtra(Utilty.DATA_FRAGMENT_BUNDLE_MOVIE_id, movieId);
                    startActivity(intent);
                }

            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movies_list_fragment_palce_holder, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_popular){
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Utilty.PREF_SORT_TYPE, Utilty.POPULAR)
                    .apply();
        }else if (id == R.id.action_top_rated){
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Utilty.PREF_SORT_TYPE, Utilty.TOP_RATED)
                    .apply();
        }else if(id == R.id.action_Favourite){
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Utilty.PREF_SORT_TYPE, Utilty.FAVOURITE)
                    .apply();
        }

        return super.onOptionsItemSelected(item);
    }
}
