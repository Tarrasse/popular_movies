package mahmoud.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

import mahmoud.com.popularmovies.modules.MoviesModule;

public class MovieDataActivity extends AppCompatActivity {

    public static final String Movie_EXTRA = "movieExtra";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_data);

        Intent income = getIntent();

        Bundle args = new Bundle();
        args.putString(Utilty.DATA_FRAGMENT_BUNDLE_SORT_TYPE, income.getStringExtra(Utilty.DATA_FRAGMENT_BUNDLE_SORT_TYPE));
        args.putLong(Utilty.DATA_FRAGMENT_BUNDLE__ID, income.getLongExtra(Utilty.DATA_FRAGMENT_BUNDLE__ID, 0));
        args.putString(Utilty.DATA_FRAGMENT_BUNDLE_MOVIE_id, income.getStringExtra(Utilty.DATA_FRAGMENT_BUNDLE_MOVIE_id));

        MovieDataFragment fragment = new MovieDataFragment();
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movie_data_palce_holder, fragment)
                .commit();

    }
}
