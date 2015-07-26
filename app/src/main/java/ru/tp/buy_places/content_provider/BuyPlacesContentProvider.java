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
import static ru.tp.buy_places.content_provider.BuyPlacesContract.Deals;
import static ru.tp.buy_places.content_provider.BuyPlacesContract.Places;
import static ru.tp.buy_places.content_provider.BuyPlacesContract.Players;

public class BuyPlacesContentProvider extends ContentProvider {
    private static final UriMatcher URI_MATCHER;


    private DatabaseHelper helper;

    private static final int PLACES = 0;
    private static final int PLACES_ID = 1;
    private static final int PLAYERS = 2;
    private static final int PLAYERS_ID = 3;
    private static final int DEALS = 4;
    private static final int DEALS_ID = 5;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME, PLACES);
        URI_MATCHER.addURI(AUTHORITY, Places.TABLE_NAME + "/#", PLACES_ID);
        URI_MATCHER.addURI(AUTHORITY, Players.TABLE_NAME, PLAYERS);
        URI_MATCHER.addURI(AUTHORITY, Players.TABLE_NAME + "/#", PLAYERS_ID);
        URI_MATCHER.addURI(AUTHORITY, Deals.TABLE_NAME, DEALS);
        URI_MATCHER.addURI(AUTHORITY, Deals.TABLE_NAME + "/#", DEALS_ID);
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
        int deleted;
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case PLACES_ID:
                final long id = ContentUris.parseId(uri);
                deleted = db.delete(Places.TABLE_NAME, Places.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(id)});
                break;
            case PLACES:
                deleted = db.delete(Places.TABLE_NAME, selection, selectionArgs);
                break;
            case PLAYERS:
                deleted = db.delete(Players.TABLE_NAME, selection, selectionArgs);
                break;
            case DEALS:
                deleted = db.delete(Deals.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return deleted;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result;
        long id;
        final SQLiteDatabase db = helper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case PLACES_ID:
            case PLACES:
                id = db.insert(Places.TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(uri, id);
                break;
            case PLAYERS_ID:
            case PLAYERS:
                id = db.insert(Players.TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(uri, id);
                break;
            case DEALS_ID:
            case DEALS:
                id = db.insert(Deals.TABLE_NAME, null, values);
                result = ContentUris.withAppendedId(Deals.CONTENT_URI, id);
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
            case PLACES_ID:
                final long placesRowId = ContentUris.parseId(uri);
                cursor = db.query(Places.WITH_OWNERS_TABLE_NAME, Places.WITH_OWNERS_COLUMNS_PROJECTION, Places.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(placesRowId)}, null, null, null, null);
                break;
            case PLACES:
                cursor = db.query(Places.WITH_OWNERS_TABLE_NAME, Places.WITH_OWNERS_COLUMNS_PROJECTION, selection, selectionArgs, null, null, sortOrder);
                break;
            case PLAYERS_ID:
                final long playersRowId = ContentUris.parseId(uri);
                cursor = db.query(Players.TABLE_NAME, Players.ALL_COLUMNS_PROJECTION, Players.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(playersRowId)}, null, null, null);
                break;
            case PLAYERS:
                cursor = db.query(Players.TABLE_NAME, Players.ALL_COLUMNS_PROJECTION, selection, selectionArgs, null, null, sortOrder);
                break;
            case DEALS_ID:
                final long dealsRowId = ContentUris.parseId(uri);
                cursor = db.query(Deals.WITH_RELATED_ENTITIES_TABLE_NAME, Deals.WITH_RELATED_ENTITIES_PROJECTION, Deals.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(dealsRowId)}, null, null, null);
                break;
            case DEALS:
                cursor = db.query(Deals.WITH_RELATED_ENTITIES_TABLE_NAME, Deals.WITH_RELATED_ENTITIES_PROJECTION, selection, selectionArgs, null, null, null);
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
            case PLACES_ID:
                final long placesId = ContentUris.parseId(uri);
                updated = db.update(Places.TABLE_NAME, values, Places.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(placesId)});
                break;
            case PLACES:
                updated = db.update(Places.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PLAYERS_ID:
                final long playersId = ContentUris.parseId(uri);
                updated = db.update(Players.TABLE_NAME, values, Players.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(playersId)});
                break;
            case PLAYERS:
                updated = db.update(Players.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DEALS_ID:
                final long dealsId = ContentUris.parseId(uri);
                updated = db.update(Deals.TABLE_NAME, values, Deals.WITH_SPECIFIED_ROW_ID_SELECTION, new String[]{Long.toString(dealsId)});
                break;
            case DEALS:
                updated = db.update(Deals.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return updated;
    }


    private static final class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "buy_places.db";
        private static final int DATABASE_VERSION = 3;


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Players.SQL_CREATE);
            db.execSQL(Places.SQL_CREATE);
            db.execSQL(Deals.SQL_CREATE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Deals.SQL_DROP);
            db.execSQL(Places.SQL_DROP);
            db.execSQL(Players.SQL_DROP);
            onCreate(db);
        }
    }
}
