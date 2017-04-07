package mahmoud.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mahmoud on 3/24/2017.
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String MOVIES_DB_NAME = "moviesdatabase.db";
    private static final int VERSION = 1;

    private final String CREATE_TABLE_POPULAR = "CREATE TABLE " + MoviesContract.PopularTable.TABLE_NAME +
            " ( " + MoviesContract.PopularTable._ID + " INTEGER PRIMARY KEY, " +
            MoviesContract.PopularTable.TITLE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.ID + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.OVERVIEW + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.RELEASE_DATE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.VOTE + " REAL NOT NULL );";

    private final String CREATE_TABLE_TOP_RATED = "CREATE TABLE " + MoviesContract.TopRatedTable.TABLE_NAME +
            " ( " + MoviesContract.PopularTable._ID + " INTEGER PRIMARY KEY, " +
            MoviesContract.PopularTable.TITLE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.ID + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.OVERVIEW + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.RELEASE_DATE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.VOTE + " REAL NOT NULL );";

    private final String CREATE_TABLE_FAVOURITE = "CREATE TABLE " + MoviesContract.FavouriteTable.TABLE_NAME +
            " ( " + MoviesContract.PopularTable._ID + " INTEGER PRIMARY KEY, " +
            MoviesContract.PopularTable.TITLE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.ID + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.OVERVIEW + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.RELEASE_DATE + " TEXT NOT NULL, " +
            MoviesContract.PopularTable.VOTE + " REAL NOT NULL );";

    public DBHelper(Context context) {
        super(context, MOVIES_DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TOP_RATED);
        sqLiteDatabase.execSQL(CREATE_TABLE_FAVOURITE);
        sqLiteDatabase.execSQL(CREATE_TABLE_POPULAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TopRatedTable.TABLE_NAME + " ;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.PopularTable.TABLE_NAME + " ;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouriteTable.TABLE_NAME + " ;");
    }
}
