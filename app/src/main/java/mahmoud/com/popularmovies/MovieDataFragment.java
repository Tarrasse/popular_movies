package mahmoud.com.popularmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import mahmoud.com.popularmovies.modules.MoviesModule;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDataFragment extends Fragment {


    private TextView titleTextView;
    private TextView dateTextView;
    private TextView ratingTextView;
    private ImageView posterImageView;

    private MoviesModule.Movie mMovie;

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
        if(args != null){
            mMovie = (MoviesModule.Movie) args.getSerializable(MovieDataActivity.Movie_EXTRA);
        }


        titleTextView = (TextView) rootView.findViewById(R.id.movie_data_title);
        dateTextView = (TextView) rootView.findViewById(R.id.movie_data_date);
        ratingTextView = (TextView) rootView.findViewById(R.id.movie_data_rating);
        posterImageView = (ImageView) rootView.findViewById(R.id.movie_data_poster);
        String title = mMovie.getTitle();
        String date = mMovie.getRelease_date().substring(0, 4);
        String rating = String.valueOf(mMovie.getVote_average()) + " / 10";
        String posterPath = mMovie.getPoster_path();

        titleTextView.setText(title);
        dateTextView.setText(date);
        ratingTextView.setText(rating);
        Picasso.with(getActivity()).load(Utilty.BASE_IMDB_IMG_URL + posterPath).into(posterImageView);



        return rootView;
    }

}
