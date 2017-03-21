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
        MoviesModule.Movie movie = (MoviesModule.Movie) income.getSerializableExtra(MovieDataActivity.Movie_EXTRA);

        Bundle aregs = new Bundle();
        aregs.putSerializable(Movie_EXTRA, movie);

        MovieDataFragment fragment = new MovieDataFragment();
        fragment.setArguments(aregs);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.movie_data_palce_holder, fragment)
                .commit();

    }
}
