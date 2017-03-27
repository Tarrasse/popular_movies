package mahmoud.com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import mahmoud.com.popularmovies.modules.MoviesModule;

public class MoviesContentProvider extends ContentProvider {
    private DBHelper dbHelper;

    private static final int POPULAR = 100;
    private static final int ALL_POPULAR = 101;
    private static final int TOP_RATED = 200;
    private static final int ALL_TOP_RATED = 201;
    private static final int FAVOURITE = 300;
    private static final int ALL_FAVOURITE = 301;

    private static final UriMatcher matcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher match = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        match.addURI(authority, MoviesContract.PATH_POPULAR, ALL_POPULAR);
        match.addURI(authority, MoviesContract.PATH_POPULAR + "/#", POPULAR);

        match.addURI(authority, MoviesContract.PATH_TOP_RATED, ALL_TOP_RATED);
        match.addURI(authority, MoviesContract.PATH_TOP_RATED + "/#", TOP_RATED);

        match.addURI(authority, MoviesContract.PATH_FAVOURITE, ALL_FAVOURITE);
        match.addURI(authority, MoviesContract.PATH_FAVOURITE + "/#", FAVOURITE);

        return match;
    }

    public MoviesContentProvider() {
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int num;
        switch (matcher.match(uri)){
            case TOP_RATED:
            case ALL_TOP_RATED:
                num = db.delete(MoviesContract.TopRatedTable.TABLE_NAME, null, null);
                break;
            case POPULAR:
            case ALL_POPULAR:
                num = db.delete(MoviesContract.PopularTable.TABLE_NAME, null, null);
                break;
            case FAVOURITE:
            case ALL_FAVOURITE:
                num = db.delete(MoviesContract.PopularTable.TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (matcher.match(uri)){
            case TOP_RATED:
            case ALL_TOP_RATED:
                id = db.insert(MoviesContract.TopRatedTable.TABLE_NAME, null,values);
                break;
            case POPULAR:
            case ALL_POPULAR:
                id = db.insert(MoviesContract.PopularTable.TABLE_NAME, null,values);
                break;
            case FAVOURITE:
            case ALL_FAVOURITE:
                id = db.insert(MoviesContract.FavouriteTable.TABLE_NAME, null,values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (matcher.match(uri)){
            case TOP_RATED:
                id = ContentUris.parseId(uri);
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.TopRatedTable.TABLE_NAME
                        + " WHERE " + MoviesContract.TopRatedTable._ID + " =" + id,
                        null);
                break;
            case ALL_TOP_RATED:
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.TopRatedTable.TABLE_NAME, null);
                break;

            case POPULAR:
                id = ContentUris.parseId(uri);
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.PopularTable.TABLE_NAME
                                + " WHERE " + MoviesContract.PopularTable._ID + " =" + id,
                        null);
                break;
            case ALL_POPULAR:
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.PopularTable.TABLE_NAME, null);
                break;

            case FAVOURITE:
                id = ContentUris.parseId(uri);
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.FavouriteTable.TABLE_NAME
                                + " WHERE " + MoviesContract.FavouriteTable._ID + " =" + id,
                        null);
                break;
            case ALL_FAVOURITE:
                returnCursor = db.rawQuery("SELECT * FROM " + MoviesContract.FavouriteTable.TABLE_NAME, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
