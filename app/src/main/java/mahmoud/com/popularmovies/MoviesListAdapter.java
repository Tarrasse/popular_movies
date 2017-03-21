package mahmoud.com.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mahmoud.com.popularmovies.modules.MoviesModule;

/**
 * Created by mahmoud on 3/20/2017.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder> {
    private static final String TAG = MoviesListAdapter.class.getSimpleName();
    private List<MoviesModule.Movie> moviesList;
    private Context mContext;
    private ItemClickListner listner;

    public MoviesListAdapter(Context context, ArrayList moviesList, ItemClickListner listner) {
        this.moviesList = moviesList;
        this.mContext = context;
        this.listner = listner;
    }

    @Override
    public MoviesListViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.OnItemClick(view, parent.indexOfChild(view));
            }
        });

        return new MoviesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesListViewHolder holder, int position) {
        MoviesModule.Movie movie = moviesList.get(position);
        String url = Utilty.BASE_IMDB_IMG_URL + movie.getPoster_path();
        Log.i(TAG, url);
        Picasso.with(mContext).load(Utilty.BASE_IMDB_IMG_URL + movie.getPoster_path()).into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (moviesList == null)
            return 0;
        return moviesList.size();
    }

    public class MoviesListViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public View view;
        public MoviesListViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.movie_list_item_poster);

        }

        public void onClick (int position){

        }

    }

    public interface ItemClickListner{
        public void OnItemClick(View v, int position);
    }

}

