package ru.tp.buy_places.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static ru.tp.buy_places.content_provider.BuyPlacesContract.AUTHORITY;
import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;

public class BuyPlacesContentProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER;


    private DatabaseHelper helper;

    private static final int PLACES = 0;
    private static final int PLACES_ID = 1;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME, PLACES);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/#", PLACES_ID);
    }

    public BuyPlacesContentProvider() {
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case PLACES:
                return Places.CONTENT_TYPE;
            case PLACES_ID:
                return Places.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        long id;
        final SQLiteDatabase db = helper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case PLACES:
                id = db.replace(Places.TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(result, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return result;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase db = helper.getReadableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case PLACES:
                cursor = db.query(Places.TABLE_NAME, Places.ALL_COLUMNS_PROJECTION, null, null, null, null, null);
                break;
            case PLACES_ID:
                cursor = db.query(Places.TABLE_NAME, Places.ALL_COLUMNS_PROJECTION, "_ID=?", new String[]{uri.getLastPathSegment()}, null, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "buy_places.db";
        private static final int DATABASE_VERSION = 1;


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Places.SQL_CREATE);
        }


        @Override
        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Places.SQL_DROP);
            db.execSQL(Places.SQL_CREATE);
            onCreate(db);
        }
    }
}
