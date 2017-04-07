package mahmoud.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
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

import mahmoud.com.popularmovies.data.MoviesContract;
import mahmoud.com.popularmovies.modules.MoviesModule;

/**
 * Created by mahmoud on 3/20/2017.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder> {
    private static final String TAG = MoviesListAdapter.class.getSimpleName();
    private Cursor moviesList;
    private Context mContext;
    private ItemClickListner listner;

    public MoviesListAdapter(Context context, Cursor moviesList, ItemClickListner listner) {
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
                moviesList.moveToPosition(parent.indexOfChild(view));
                String movieId = moviesList.getString(moviesList.getColumnIndex(MoviesContract.PopularTable.ID));
                int _id = moviesList.getInt(moviesList.getColumnIndex(MoviesContract.PopularTable._ID));
                listner.OnItemClick(view, parent.indexOfChild(view), movieId, _id);
            }
        });

        return new MoviesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesListViewHolder holder, int position) {
        moviesList.moveToPosition(position);
        String posterPath = moviesList.getString(moviesList.getColumnIndex(MoviesContract.PopularTable.POSTER_PATH));

        String url = Utilty.BASE_IMDB_IMG_URL + posterPath;
        Log.i(TAG, url);
        Picasso.with(mContext).load(Utilty.BASE_IMDB_IMG_URL + posterPath).into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (moviesList == null)
            return 0;
        return moviesList.getCount();
    }

    public class MoviesListViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;
        public View view;
        public MoviesListViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.movie_list_item_poster);

        }
    }

    public void swapCursor(Cursor newCursor){
        moviesList = newCursor;
        notifyDataSetChanged();
    }

    public interface ItemClickListner{
        public void OnItemClick(View v, int position, String movieId, int _id);
    }



}

