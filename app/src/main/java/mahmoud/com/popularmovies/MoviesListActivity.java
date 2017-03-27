package mahmoud.com.popularmovies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import mahmoud.com.popularmovies.modules.MoviesModule;
import mahmoud.com.popularmovies.sync.MoviesSyncAdapter;

public class MoviesListActivity extends AppCompatActivity {

    final String AUTHORITY = "mahmoud.com.popularmovies";
    final String ACCOUNT_TYPE = "mahmoud.com.popularmovies";
    final String ACCOUNT = "syncAdapterAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MoviesSyncAdapter.initializeSyncAdapter(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movies_list_fragment_palce_holder, new MoviesListFragment())
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
        }

        return super.onOptionsItemSelected(item);
    }
}
