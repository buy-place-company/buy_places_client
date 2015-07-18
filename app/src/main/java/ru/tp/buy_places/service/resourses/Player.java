package ru.tp.buy_places.service.resourses;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONObject;

import ru.tp.buy_places.content_provider.BuyPlacesContract;

/**
 * Created by Ivan on 17.05.2015.
 */
public class Player implements Resource {
    private final long mId;
    private final String mUsername;
    private final int mLevel;
    private final String mAvatar;
    private final long mCash;
    private final long mScore;
    private final int mPlaces;
    private final int mMaxPlaces;
    private long mRowId;

    public long getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getLevel() {
        return mLevel;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public long getScore() {
        return mScore;
    }

    public int getPlaces() {
        return mPlaces;
    }

    public int getMaxPlaces() {
        return mMaxPlaces;
    }

    public long getRowId() {
        return mRowId;
    }

    public Player(long id, String username, int level, String avatar, long cash, long score, int places, int maxPlaces) {
        mId = id;
        mUsername = username;
        mLevel = level;
        mAvatar = avatar;
        mCash = cash;
        mScore = score;
        mPlaces = places;
        mMaxPlaces = maxPlaces;
    }

    public Player(long rowId, long id, String username, int level, String avatar, long cash, long score, int venuesCount, int maxPlaces) {
        this(id, username, level, avatar, cash, score, venuesCount, maxPlaces);
        mRowId = rowId;
    }


    public static Player fromJSONObject(JSONObject playerJSONObject) {
        String username = playerJSONObject.optString("username");
        int venuesCount = playerJSONObject.optInt("objects_count");
        String avatar = playerJSONObject.optString("avatar");
        int level = playerJSONObject.optInt("level");
        long cash = playerJSONObject.optLong("cash");
        long score = playerJSONObject.optLong("score");
        int maxPlaces = playerJSONObject.optInt("max_objects");
        long id = playerJSONObject.optLong("id");
        return new Player(id, username, level, avatar, cash, score, venuesCount, maxPlaces);
    }

    public static Player fromCursor(Cursor cursor) {
        long rowId = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players._ID));
        long id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_USERNAME));
        int level = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_LEVEL));
        String avatar = cursor.getString(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_AVATAR));
        long cash = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_CASH));
        long score = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_SCORE));
        int places = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_PLACES));
        int maxPlaces = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_MAX_PLACES));
        return new Player(rowId, id, username, level, avatar, cash, score, places, maxPlaces);
    }

    public long writeToDatabase(Context context) {
        ContentValues values = new ContentValues();
        values.put(BuyPlacesContract.Players.COLUMN_ID, mId);
        values.put(BuyPlacesContract.Players.COLUMN_USERNAME, mUsername);
        values.put(BuyPlacesContract.Players.COLUMN_LEVEL, mLevel);
        values.put(BuyPlacesContract.Players.COLUMN_AVATAR, mAvatar);
        values.put(BuyPlacesContract.Players.COLUMN_CASH, mCash);
        values.put(BuyPlacesContract.Players.COLUMN_SCORE, mScore);
        values.put(BuyPlacesContract.Players.COLUMN_PLACES, mPlaces);
        values.put(BuyPlacesContract.Players.COLUMN_MAX_PLACES, mMaxPlaces);

        long id;

        if (context.getContentResolver().update(BuyPlacesContract.Players.CONTENT_URI, values, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}) == 0) {
            Uri uri = context.getContentResolver().insert(BuyPlacesContract.Players.CONTENT_URI, values);
            id = ContentUris.parseId(uri);
        } else {
            Cursor cursor = context.getContentResolver().query(BuyPlacesContract.Players.CONTENT_URI, BuyPlacesContract.Players.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}, null);
            cursor.moveToFirst();
            id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players._ID));
            cursor.close();
        }
        return id;
    }

    public long getCash() {
        return mCash;
    }
}
