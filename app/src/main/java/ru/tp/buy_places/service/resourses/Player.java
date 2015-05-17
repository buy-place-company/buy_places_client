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
    private final long mScore;
    private final int mPlaces;
    private final int mMaxPlaces;
    private long mRowId;

    private Player(long id, String username, int level, String avatar, long score, int places, int maxPlaces) {
        mId = id;
        mUsername = username;
        mLevel = level;
        mAvatar = avatar;
        mScore = score;
        mPlaces = places;
        mMaxPlaces = maxPlaces;
    }

    public Player(long rowId, long id, String username, int level, String avatar, long score, int places, int maxPlaces) {
        this(id, username, level, avatar, score, places, maxPlaces);
        mRowId = rowId;
    }


    public static Player fromJSONObject(JSONObject playerJSONObject) {
        long id = playerJSONObject.optLong("id");
        String username = playerJSONObject.optString("username");
        int level = playerJSONObject.optInt("level");
        String avatar = playerJSONObject.optString("avatar");
        long score = playerJSONObject.optLong("score");
        int places = playerJSONObject.optInt("places");
        int maxPlaces = playerJSONObject.optInt("max_places");
        return new Player(id, username, level, avatar, score, places, maxPlaces);
    }

    public static Player fromCursor(Cursor cursor) {
        long rowId = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players._ID));
        long id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_USERNAME));
        int level = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_LEVEL));
        String avatar = cursor.getString(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_AVATAR));
        long score = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_SCORE));
        int places = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_PLACES));
        int maxPlaces = cursor.getInt(cursor.getColumnIndex(BuyPlacesContract.Players.COLUMN_MAX_PLACES));
        return new Player(rowId, id, username, level, avatar, score, places, maxPlaces);
    }

    public long writeToDatabase(Context context) {
        ContentValues values = new ContentValues();
        values.put(BuyPlacesContract.Players.COLUMN_ID, mId);
        values.put(BuyPlacesContract.Players.COLUMN_USERNAME, mUsername);
        values.put(BuyPlacesContract.Players.COLUMN_LEVEL, mLevel);
        values.put(BuyPlacesContract.Players.COLUMN_AVATAR, mAvatar);
        values.put(BuyPlacesContract.Players.COLUMN_SCORE, mScore);
        values.put(BuyPlacesContract.Players.COLUMN_PLACES, mPlaces);
        values.put(BuyPlacesContract.Players.COLUMN_MAX_PLACES, mMaxPlaces);

        long id;

        if (context.getContentResolver().update(BuyPlacesContract.Players.CONTENT_URI, values, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}) == 0) {
            Uri uri = context.getContentResolver().insert(BuyPlacesContract.Players.CONTENT_URI, values);
            id = ContentUris.parseId(uri);
        } else {
            Cursor cursor = context.getContentResolver().query(BuyPlacesContract.Players.CONTENT_URI, BuyPlacesContract.Players.ALL_COLUMNS_PROJECTION, BuyPlacesContract.Players.WITH_SPECIFIED_ID_SELECTION, new String[]{Long.toString(mId)}, null);
            id = cursor.getLong(cursor.getColumnIndex(BuyPlacesContract.Players._ID));
        }
        return id;
    }
}
