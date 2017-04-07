package mahmoud.com.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahmoud on 3/24/2017.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "mahmoud.com.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_TOP_RATED = "toprated";
    public static final String PATH_FAVOURITE = "favourite";

    public static class PopularTable implements BaseColumns {
        public static final String TABLE_NAME = "PopularTable";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String VOTE = "vote";
        public static final String POSTER_PATH = "poster";
        public static final String ID = "movieid";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        public static Uri buildPopularUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static class TopRatedTable implements BaseColumns {
        public static final String TABLE_NAME = "TopRatedTable";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "date";
        public static final String OVERVIEW = "overview";
        public static final String VOTE = "vote";
        public static final String POSTER_PATH = "poster";
        public static final String ID = "movieid";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build();

        public static Uri buildTopRatedUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static class FavouriteTable implements BaseColumns {
        public static final String TABLE_NAME = "FavouriteTable";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE = "vote";
        public static final String COLUMN_POSTER_PATH = "poster";
        public static final String COLUMN_ID = "movieid";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }



}
