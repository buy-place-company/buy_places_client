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
    private static final int PLACES_AROUND_THE_PLAYER = 2;
    private static final int PLACES_AROUND_THE_POINT = 3;
    private static final int PLACES_VISITED_IN_THE_PAST = 4;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME, PLACES);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/#", PLACES_ID);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/" + Places.AROUND_THE_PLAYER_DATA_SET, PLACES_AROUND_THE_PLAYER);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/" + Places.AROUND_THE_POINT_DATA_SET, PLACES_AROUND_THE_POINT);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/" + Places.VISITED_IN_THE_PAST_DATA_SET, PLACES_VISITED_IN_THE_PAST);
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
                Cursor cursor = db.query(Places.TABLE_NAME, Places.ALL_COLUMNS_PROJECTION, Places.WITH_SPECIFIED_ID_SELECTION, new String[]{values.getAsString(Places.COLUMN_ID)}, null, null, null);
                if (cursor.getCount() == 0) {
                    id = db.insert(Places.TABLE_NAME, null, values);
                    result = ContentUris.withAppendedId(uri, id);
                } else {
                    cursor.moveToFirst();
                    final Places.State currentState = Places.State.valueOf(cursor.getString(cursor.getColumnIndex(Places.COLUMN_STATE)));
                    final Places.State newState = Places.State.valueOf(values.getAsString(Places.COLUMN_STATE));
                    final Places.State state = currentState.ordinal() > newState.ordinal() ? currentState : newState;
                    values.put(Places.COLUMN_STATE, state.name());
                    db.update(Places.TABLE_NAME, values, Places.WITH_SPECIFIED_ID_SELECTION, new String[]{values.getAsString(Places.COLUMN_ID)});
                    final long rowId = cursor.getLong(cursor.getColumnIndex(Places._ID));
                    result = ContentUris.withAppendedId(uri, rowId);
                }
                cursor.close();
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
        int updated;
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case PLACES_AROUND_THE_POINT:
                updated = db.update(Places.TABLE_NAME, values, Places.WITH_SPECIFIED_STATE_SELECTION, new String[]{Places.State.AROUND_THE_POINT.name()});
                break;
            case PLACES_AROUND_THE_PLAYER:
                updated = db.update(Places.TABLE_NAME, values, Places.WITH_SPECIFIED_STATE_SELECTION, new String[]{Places.State.AROUND_THE_PLAYER.name()});
                break;
            case PLACES_VISITED_IN_THE_PAST:
                updated = db.update(Places.TABLE_NAME, values, Places.WITH_SPECIFIED_STATE_SELECTION, new String[]{Places.State.VISITED_IN_THE_PAST.name()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return updated;
    }


    private static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "buy_places.db";
        private static final int DATABASE_VERSION = 2;


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
            onCreate(db);
        }
    }
}
